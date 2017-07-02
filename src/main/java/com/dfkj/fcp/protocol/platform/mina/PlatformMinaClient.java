package com.dfkj.fcp.protocol.platform.mina;

import com.dfkj.fcp.config.SystemConfig;
import com.dfkj.fcp.core.encrypt.SSLContextGenerator;
import com.dfkj.fcp.core.logger.AcpLogger;
import com.dfkj.fcp.core.mina.MinaDefaultCodecFactory;
import com.dfkj.fcp.core.mina.SimpleTextLineCodecFilter;
import com.dfkj.fcp.core.vo.Message;
import com.dfkj.fcp.protocol.platform.factory.PlatformInstanceFactory;
import com.dfkj.fcp.protocol.platform.mq.PlatformOfflineMessageConsumer;
import com.dfkj.fcp.protocol.platform.task.DeviceHeatRunnable;
import com.dfkj.fcp.protocol.platform.task.PlatformHeatRunnable;
import com.dfkj.fcp.protocol.platform.vo.PlatformConnectStatus;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoService;
import org.apache.mina.core.service.IoServiceListener;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.ssl.SslFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;


/**
 * Mina平台客户端
 * Created by JiangWenGuang on 2017/4/21.
 */
public class PlatformMinaClient {

    // 连接超时时间
    public final static int CON_TIMEOUT_MILLIS = 3 * 1000;

    private IoConnector connector = null;
    private IoSession session = null;

    // 网关心跳任务
    Runnable heartRunnable = null;
    // 网关心跳线程
    Thread heartThread = null;
    // 设备心跳任务
    Runnable deviceHeartRunnable = null;
    // 设备心跳线程
    Thread deviceHeartThread = null;
    // 离线消息消费者
    PlatformOfflineMessageConsumer consumer;
    // 离线消息消费者线程
    Thread consumerThread = null;
    // 设备信息
    public static ConcurrentHashMap<String, Date> deviceMap = new ConcurrentHashMap<>();

    private static AcpLogger logger = new AcpLogger(PlatformMinaClient.class);

    public PlatformMinaClient(){
        init();
    }

    public void init(){

        connector = createPlatformCon();

        connector.addListener(new IoServiceListener() {

            public void serviceActivated(IoService service) throws Exception {}

            public void serviceIdle(IoService service, IdleStatus idleStatus) throws Exception {}

            public void serviceDeactivated(IoService service) throws Exception {}

            public void sessionCreated(IoSession session) throws Exception {}

            public void sessionClosed(IoSession session) throws Exception {

                logger.error("<<<<PlatformMinaClient sessionClosed>>>>");

            }

            public void sessionDestroyed(IoSession session_) throws Exception {

                logger.error("<<<<PlatformMinaClient sessionDestroyed>>>>");

                if(session_.isConnected()){
                    session_.closeOnFlush();
                }

            }
        });

    }

    /**
     * 连接平台
     */
    public void connect(){
        PlatformConnectStatus.setConnectStatus(PlatformConnectStatus.CONNECT_DISCONNECT);
        ConnectFuture future = connector.connect(new InetSocketAddress(SystemConfig.getProperty("platform.address"), Integer.parseInt(SystemConfig.getProperty("platform.port"))));
        future.awaitUninterruptibly();
        session = future.getSession();
        if (session != null) {
            logger.debug("连接平台:" + SystemConfig.getProperty("platform.address") + " 成功！");
            if (session.isConnected()) {
                PlatformConnectStatus.setConnectStatus(PlatformConnectStatus.CONNECT_SUCCESS);
                heartRunnable = new PlatformHeatRunnable(session, 5 * 1000);
                heartThread = new Thread(heartRunnable);
                heartThread.setPriority(Thread.MAX_PRIORITY);
                heartThread.start();
                // 开启自动发送设备报文
                if(SystemConfig.PLATFORM_AUTO_SEND_DEVICE_MESSAGE_OPEN.equals(SystemConfig.getProperty(SystemConfig.PLATFORM_AUTO_SEND_DEVICE_MESSAGE_KEY))){
                    deviceHeartRunnable = new DeviceHeatRunnable(session, 5*1000);
                    deviceHeartThread = new Thread(deviceHeartRunnable);
                    deviceHeartThread.setPriority(Thread.NORM_PRIORITY);
                    deviceHeartThread.start();
                }
                // 开启离线存储
                if(SystemConfig.PLATFORM_OFFLINE_STORAGE_OPEN.equals(SystemConfig.getProperty(SystemConfig.PLATFORM_OFFLINE_STORAGE_KEY))){
                    try {
                        consumer = new PlatformOfflineMessageConsumer(SystemConfig.getProperty(SystemConfig.GATEWAY_ID_KEY));
                        consumerThread = new Thread(consumer);
                        heartThread.setPriority(Thread.NORM_PRIORITY);
                        consumerThread.start();
                    }catch (Exception e){
                        e.printStackTrace();
                        logger.error(e.getMessage());
                    }
                }
            }
        }

    }

    /**
     * 销毁资源
     */
    public void destroy(){
        if(session != null){
            if(session.isConnected()){
                session.closeOnFlush();
                session = null;
            }
        }
        if(connector != null){
            if(!connector.isDisposed()){
                connector.dispose();
                connector = null;
            }
        }
        if(heartThread != null){
            if(!heartThread.isInterrupted()){
                heartThread.interrupt();
                heartThread = null;
            }
        }
        // 开启自动发送设备报文
        if(SystemConfig.PLATFORM_AUTO_SEND_DEVICE_MESSAGE_OPEN.equals(SystemConfig.getProperty(SystemConfig.PLATFORM_AUTO_SEND_DEVICE_MESSAGE_KEY))){
            if(deviceHeartThread != null){
                if(!deviceHeartThread.isInterrupted()){
                    deviceHeartThread.interrupt();
                    deviceHeartThread = null;
                }
            }
        }
        // 开启离线存储
        if(SystemConfig.PLATFORM_OFFLINE_STORAGE_OPEN.equals(SystemConfig.getProperty(SystemConfig.PLATFORM_OFFLINE_STORAGE_KEY))){
            if(consumer != null){
                try {
                    consumer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error(e.getMessage());
                }
                if(!consumerThread.isInterrupted()){
                    consumerThread.interrupt();
                }
            }
        }
    }

    /**
     * 监听硬件Mina服务器的数据,并发给平台客户端
     *
     * @param message 消息
     */
    public void listenHardwareServerSendMessage(Message message){

        if(message != null){
            // 与平台连接处于断线中
            if(PlatformConnectStatus.getConnectStatus() != PlatformConnectStatus.CONNECT_SUCCESS
                    || session == null || !session.isConnected()){
                // 开启离线存储
                if(SystemConfig.PLATFORM_OFFLINE_STORAGE_OPEN.equals(SystemConfig.getProperty(SystemConfig.PLATFORM_OFFLINE_STORAGE_KEY))){
                    try{
                        PlatformInstanceFactory.getOffMessageProducerInstance().sendMessage(message);
                    }catch(Exception e){
                        e.printStackTrace();
                        logger.error(e.getMessage());
                    }
                }
            }else{
                if(session != null){
                    session.write(message);
                }
            }
        }

    }

    /**
     * 创建于平台的连接
     *
     * @return
     */
    private static IoConnector createPlatformCon() {
        IoConnector connector = new NioSocketConnector();
        SslFilter sslFilter = new SslFilter(new SSLContextGenerator().getSslContext());
        sslFilter.setUseClientMode(true);
        connector.getFilterChain().addLast("simpleTextLineCodec", new SimpleTextLineCodecFilter());
        //connector.getFilterChain().addLast("crypto", new AESFilter());
        connector.getFilterChain().addLast("codec", new PlatformCodeFilter());
        connector.getFilterChain().addFirst("@default_codec@", new ProtocolCodecFilter(new MinaDefaultCodecFactory()));
        connector.getFilterChain().addFirst("sslFilter", sslFilter);
        connector.getFilterChain().addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));

        connector.setHandler(new PlatformClientHandler());
        connector.setConnectTimeoutMillis(CON_TIMEOUT_MILLIS);

        return connector;
    }

    public static ConcurrentHashMap<String, Date> getDeviceMap() {
        return deviceMap;
    }

    public static void addDevice(String deviceNo) {
        PlatformMinaClient.deviceMap.put(deviceNo, new Date());
    }
}
