package com.dfkj.fcp.protocol.platform.factory;

import com.dfkj.fcp.config.SystemConfig;
import com.dfkj.fcp.core.logger.AcpLogger;
import com.dfkj.fcp.protocol.platform.mina.PlatformMinaClient;
import com.dfkj.fcp.protocol.platform.mq.PlatformOfflineMessageProducer;

/**
 * 实例工厂
 *
 * Created by JiangWenGuang on 2017/4/27.
 */
public final class PlatformInstanceFactory {

    private final static AcpLogger logger = new AcpLogger(PlatformInstanceFactory.class);

    private PlatformInstanceFactory(){}

    private static PlatformMinaClient instance;
    private static PlatformOfflineMessageProducer producer = null;

    public synchronized static PlatformMinaClient getMinaClientInstance() {
        return instance;
    }

    public synchronized static void newMinaClientInstance() {
        instance = new PlatformMinaClient();
    }


    public synchronized static PlatformOfflineMessageProducer getOffMessageProducerInstance() {
        return producer;
    }

    public synchronized static void newOffMessageProducerInstance() {
        try{
            producer = new PlatformOfflineMessageProducer(SystemConfig.getProperty(SystemConfig.GATEWAY_ID_KEY));
        }catch(Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }
}
