package com.dfkj.fcp.protocol.hardware.mina;

import java.awt.List;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;

import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.dfkj.fcp.config.SystemConfig;
import com.dfkj.fcp.core.logger.AcpLogger;
import com.dfkj.fcp.core.mina.MinaDefaultCodecFactory;
import com.dfkj.fcp.core.vo.HelloMessage;

/**
 * Mina硬件服务端
 * 主要用来接收硬件上传的报文数据
 *
 * Created by JiangWenGuang on 2017/4/22.
 */
public class HardwareMinaServer {

    private final static AcpLogger logger = new AcpLogger(HardwareMinaServer.class);

    private IoAcceptor acceptor = null;
    
    Map<Long,HelloMessage> terminalsMap = new HashMap();

    public HardwareMinaServer(){
        init();
    }

    public void init() {
        acceptor = createHardwareAcceptor();
       
    }
    
    public HelloMessage getTerminalState(long msgId){
    	return terminalsMap.get(msgId);
    };
    
    public void putTerminalState(long msgId,HelloMessage msg) {
    	terminalsMap.put(msgId, msg);
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
     * 创建硬件服务器接收对象
     *
     * @return
     */
    private IoAcceptor createHardwareAcceptor() {
        acceptor = new NioSocketAcceptor();
        /*SslFilter sslFilter = new SslFilter(new SSLContextGenerator().getSslContext());
        sslFilter.setUseClientMode(true);*/
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
