package com.dfkj.fcp.protocol.platform.task;

import com.dfkj.fcp.core.constant.EMessageType;
import com.dfkj.fcp.core.logger.AcpLogger;
import com.dfkj.fcp.core.vo.Message;
import com.dfkj.fcp.protocol.platform.vo.PlatformConnectStatus;

import org.apache.mina.core.session.IoSession;

import java.util.Date;

/**
 * 平台心跳报文任务
 *
 * Created by JiangWenGuang on 2017/4/21.
 */
public class PlatformHeatRunnable implements Runnable {

    private static AcpLogger logger = new AcpLogger(PlatformHeatRunnable.class);

    private long millisecond;
    private IoSession session;

    public PlatformHeatRunnable(IoSession session, long millisecond){
        this.session  = session;
        this.millisecond = millisecond;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Message hb = new Message();
                hb.setMsgType(EMessageType.GATEWAY_HEARTBEAT);
                session.write(hb);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            Date currentTime = new Date();
            if((currentTime.getTime() - PlatformConnectStatus.getHeartTime()) > 5 * 60 * 1000){
                logger.error("心跳超时!");
            }
            try {
                Thread.sleep(millisecond);
            } catch (InterruptedException e) {
                //e.printStackTrace();
                logger.error("[" + Thread.currentThread().getName() + "] is interrupt!");
            }
        }

    }

}
