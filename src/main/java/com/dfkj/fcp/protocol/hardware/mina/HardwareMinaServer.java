package com.dfkj.fcp.protocol.hardware.mina;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.ssl.SslFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.dfkj.fcp.config.SystemConfig;
import com.dfkj.fcp.core.encrypt.SSLContextGenerator;
import com.dfkj.fcp.core.logger.AcpLogger;
import com.dfkj.fcp.core.mina.MinaDefaultCodecFactory;
import com.dfkj.fcp.core.vo.Message;

/**
 * Mina硬件服务端
 * 主要用来接收硬件上传的报文数据
 *
 * Created by JiangWenGuang on 2017/4/22.
 */
public class HardwareMinaServer {

    private final static AcpLogger logger = new AcpLogger(HardwareMinaServer.class);

    private IoAcceptor acceptor = null;

    /**
     * 这个消息队列用来接收Mina平台客户端发送的数据
     */
    private Map<Integer, LinkedBlockingQueue<Message>> messageQueue = null;

    public HardwareMinaServer(){
        init();
    }

    public void init() {
        acceptor = createHardwareAcceptor();
        messageQueue = new ConcurrentHashMap<Integer, LinkedBlockingQueue<Message>>();
    }

    /**
     * 是否包含消息
     *
     * @param id
     * @return
     */
    public boolean hasMessage(int id) {
        if (messageQueue.containsKey(id)) {
            return messageQueue.get(id).size() > 0 ? true : false;
        }
        return false;
    }

    /**
     * 取出消息
     *
     * @param id
     * @return
     */
    public Message take(int id) {
        if (hasMessage(id)) {
            try {
                return messageQueue.get(id).take();
            } catch (InterruptedException e) {
                logger.debug(String.format("消息队列中获取消息失败,原因:", e.getMessage()));
            }
        }
        return null;
    }

    /**
     * 放入消息
     *
     * @param message
     */
    public void put(Message message) {
        if (!hasMessage(message.hashCode())) {
            messageQueue.put(message.hashCode(), new LinkedBlockingQueue<Message>());
        }
        try {
            messageQueue.get(message.hashCode()).put(message);
        } catch (InterruptedException e) {
            logger.debug(String.format("消息队列中添加消息失败，原因:", e.getMessage()));
        }
    }

    /**
     * 绑定接口,启动Mina服务端
     * @throws Exception
     */
    public void startUp() throws Exception{
        try {
            acceptor.bind(new InetSocketAddress(SystemConfig.getProperty(SystemConfig.DEVICE_ADDRESS_KEY),Integer.parseInt(SystemConfig.getProperty(SystemConfig.DEVICE_PORT_KEY))));
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 监听硬件Mina服务器的数据,并发给平台客户端
     *
     * @param message 消息
     */
    public void listenPlatformClientSendMessage(Message message){

        if(message != null){
            put(message);
        }

    }

    /**
     * 创建硬件服务器接收对象
     *
     * @return
     */
    private IoAcceptor createHardwareAcceptor() {
        acceptor = new NioSocketAcceptor();
        SslFilter sslFilter = new SslFilter(new SSLContextGenerator().getSslContext());
        sslFilter.setUseClientMode(true);
        acceptor.getSessionConfig().setReadBufferSize(2048);
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);

        acceptor.getFilterChain().addLast("codec", new HardwareCodeFilter());
        /*acceptor.getFilterChain().addLast("msg_process", new HardwareMessageFilter());*/
        acceptor.getFilterChain().addFirst("@default_codec@", new ProtocolCodecFilter(new MinaDefaultCodecFactory()));
        //acceptor.getFilterChain().addFirst("sslFilter", sslFilter);

        acceptor.setHandler(new HardwareServerHandler());

        return acceptor;
    }
}
