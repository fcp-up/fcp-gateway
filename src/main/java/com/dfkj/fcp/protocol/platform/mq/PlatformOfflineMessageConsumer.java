package com.dfkj.fcp.protocol.platform.mq;

import com.dfkj.fcp.core.logger.AcpLogger;
import com.dfkj.fcp.core.mq.EndPoint;
import com.dfkj.fcp.core.vo.Message;
import com.dfkj.fcp.protocol.platform.factory.PlatformInstanceFactory;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import org.springframework.util.SerializationUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 功能概要：读取队列的程序端，实现了Runnable接口
 *
 * Created by JiangWenGuang on 2017/4/26.
 */
public class PlatformOfflineMessageConsumer extends EndPoint implements Runnable, Consumer {

    private final static AcpLogger logger = new AcpLogger(PlatformOfflineMessageConsumer.class);

    public PlatformOfflineMessageConsumer(String endPointName) throws IOException,TimeoutException{
        super(endPointName);
    }

    public void run() {
        try {
            //start consuming messages. Auto acknowledge messages.
            channel.basicConsume(endPointName, true,this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when consumer is registered.
     */
    public void handleConsumeOk(String consumerTag) {
        logger.info("PlatformOfflineMessageConsumer Consumer "+consumerTag +" registered");
    }

    /**
     * Called when new message is available.
     */
    public void handleDelivery(String consumerTag, Envelope env,
                               AMQP.BasicProperties props, byte[] body) throws IOException {
        Message msg = (Message) SerializationUtils.deserialize(body);
        logger.info("PlatformOfflineMessageConsumer Message: "+ msg.toString() + " received.");
        PlatformInstanceFactory.getMinaClientInstance().listenHardwareServerSendMessage(msg);
    }

    public void handleCancel(String consumerTag) {}
    public void handleCancelOk(String consumerTag) {}
    public void handleRecoverOk(String consumerTag) {}
    public void handleShutdownSignal(String consumerTag, ShutdownSignalException arg1) {}
}
