package com.dfkj.fcp.protocol.platform.task;

import com.dfkj.fcp.config.SystemConfig;
import com.dfkj.fcp.core.constant.EMessageType;
import com.dfkj.fcp.core.logger.AcpLogger;
import com.dfkj.fcp.core.vo.Message;
import com.dfkj.fcp.protocol.platform.mina.PlatformMinaClient;

import org.apache.mina.core.session.IoSession;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 设备心跳报文任务
 *
 * Created by JiangWenGuang on 2017/4/21.
 */
public class DeviceHeatRunnable implements Runnable {

    private static AcpLogger logger = new AcpLogger(DeviceHeatRunnable.class);

    private long millisecond;
    private IoSession session;

    public DeviceHeatRunnable(IoSession session, long millisecond){
        this.session  = session;
        this.millisecond = millisecond;
    }

    @Override
    public void run() {
        while (true) {
            ConcurrentHashMap<String,Date> deviceMap = PlatformMinaClient.getDeviceMap();
            try {
                for(String key : deviceMap.keySet()){
                    int timeout = Integer.parseInt(SystemConfig.getProperty(SystemConfig.PLATFORM_DEVICE_NO_RESPONSE_TIMEOUT_KEY));
                    Date currentTime = new Date();
                    if((currentTime.getTime() - deviceMap.get(key).getTime()) > timeout * 1000){
                        logger.error("设备["+key+"]心跳超时!");
                        continue;
                    }
                    Message hb = new Message();
                    hb.setDeviceNo(key);
                    hb.setMsgType(EMessageType.HEARTBEAT);
                    session.write(hb);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
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
