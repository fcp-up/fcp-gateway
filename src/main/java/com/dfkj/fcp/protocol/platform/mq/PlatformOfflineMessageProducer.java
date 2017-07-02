package com.dfkj.fcp.protocol.platform.mq;

import com.dfkj.fcp.core.logger.AcpLogger;
import com.dfkj.fcp.core.mq.EndPoint;
import com.dfkj.fcp.protocol.hardware.mina.HardwareMinaServer;

import org.springframework.util.SerializationUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeoutException;

/**
 * 功能概要：消息生产者
 *
 * Created by JiangWenGuang on 2017/4/26.
 */
public class PlatformOfflineMessageProducer extends EndPoint {

    private final static AcpLogger logger = new AcpLogger(PlatformOfflineMessageProducer.class);

    public PlatformOfflineMessageProducer(String endPointName) throws IOException,TimeoutException {
        super(endPointName);
    }

    public void sendMessage(Serializable object) throws IOException ,TimeoutException{
        channel.basicPublish("",endPointName, null, SerializationUtils.serialize(object));
    }
}
