package com.dfkj.fcp.protocol.platform.task;

import com.dfkj.fcp.config.SystemConfig;
import com.dfkj.fcp.core.logger.AcpLogger;
import com.dfkj.fcp.protocol.platform.factory.PlatformInstanceFactory;
import com.dfkj.fcp.protocol.platform.vo.PlatformConnectStatus;

/**
 * 连接平台任务
 *
 * Created by JiangWenGuang on 2017/4/21.
 */
public class PlatformConnectionRunnable implements Runnable {

    private final static AcpLogger logger = new AcpLogger(PlatformConnectionRunnable.class);

    // 重连时间
    public final static long RECON_MILLIS = 6 * 1000;

    public PlatformConnectionRunnable(){
        PlatformInstanceFactory.newMinaClientInstance();
        if(SystemConfig.PLATFORM_OFFLINE_STORAGE_OPEN.equals(SystemConfig.getProperty("platform.offline.storage"))){
            PlatformInstanceFactory.newOffMessageProducerInstance();
        }
    }

    @Override
    public void run() {
        PlatformConnectStatus.setGatewayId(SystemConfig.getProperty("gateway.id"));
        while(true){
            try {
                if (PlatformConnectStatus.CONNECT_DISCONNECT == PlatformConnectStatus.getConnectStatus()) {
                    logger.info("[[[    ---- 平台服务断线重连 ----   ]]]");
                    PlatformConnectStatus.setConnectStatus(PlatformConnectStatus.CONNECT_RECONNECTION);
                    if(PlatformInstanceFactory.getMinaClientInstance() != null){
                        PlatformInstanceFactory.getMinaClientInstance().destroy();
                    }
                    PlatformInstanceFactory.newMinaClientInstance();
                    PlatformInstanceFactory.getMinaClientInstance().connect();
                } else if (PlatformConnectStatus.CONNECT_INIT == PlatformConnectStatus.getConnectStatus()) {
                    logger.info("[[[    ---- 平台服务第一次连接 ----   ]]]");
                    PlatformInstanceFactory.getMinaClientInstance().connect();
                } else if(PlatformConnectStatus.CONNECT_RECONNECTION == PlatformConnectStatus.getConnectStatus()){
                    logger.info("[[[    ---- 平台服务正在重连连接中 ----   ]]]");
                }
            }catch (Exception e){
                logger.error(e.getMessage());
                PlatformConnectStatus.setConnectStatus(PlatformConnectStatus.CONNECT_DISCONNECT);
            }

            try {
                Thread.sleep(PlatformConnectionRunnable.RECON_MILLIS);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }

}
