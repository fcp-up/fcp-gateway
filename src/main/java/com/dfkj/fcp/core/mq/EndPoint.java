package com.dfkj.fcp.core.mq;

import com.dfkj.fcp.config.SystemConfig;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *
 * 功能概要： EndPoint类型的队列
 *
 * @author JiangWenGuang
 * @since  2016年1月11日
 */
public abstract class EndPoint {

    protected Channel channel;
    protected Connection connection;
    protected String endPointName;

    public EndPoint(String endpointName) throws IOException,TimeoutException {
        this.endPointName = endpointName;

        //Create a connection factory
        ConnectionFactory factory = new ConnectionFactory();

        //hostname of your rabbitmq server
        factory.setHost(SystemConfig.getProperty(SystemConfig.RABBITMQ_HOST_KEY));
        factory.setPort(Integer.parseInt(SystemConfig.getProperty(SystemConfig.RABBITMQ_PORT_KEY)));
        factory.setUsername(SystemConfig.getProperty(SystemConfig.RABBITMQ_USERNAME_KEY));
        factory.setPassword(SystemConfig.getProperty(SystemConfig.RABBITMQ_PASSWORD_KEY));

        //getting a connection
        connection = factory.newConnection();

        //creating a channel
        channel = connection.createChannel();

        //declaring a queue for this channel. If queue does not exist,
        //it will be created on the server.
        channel.queueDeclare(endpointName, false, false, false, null);
    }


    /**
     * 关闭channel和connection。并非必须，因为隐含是自动调用的。
     * @throws IOException,TimeoutException
     */
    public void close() throws IOException,TimeoutException{
        if(this.channel.isOpen()){
            this.channel.close();
        }
        if(this.connection.isOpen()){
            this.connection.close();
        }
    }
}
