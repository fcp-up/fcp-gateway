package com.dfkj.fcp.protocol.hardware.factory;

import com.dfkj.fcp.protocol.hardware.mina.HardwareMinaServer;

/**
 * 实例工厂
 *
 * Created by JiangWenGuang on 2017/4/23.
 */
public final class HardwareInstanceFactory {

    private static HardwareMinaServer instance;

    public synchronized static HardwareMinaServer getMinaServerInstance() {
        return instance;
    }

    public synchronized static void newMinaServerInstance() {
        instance = new HardwareMinaServer();
    }
}
