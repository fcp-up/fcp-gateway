package com.dfkj.fcp.protocol.platform.vo;

import java.util.Date;

/**
 * 平台连接状态
 *
 * Created by JiangWenGuang on 2017/4/21.
 */
public final class PlatformConnectStatus {

    public final static int CONNECT_INIT = 0; // 0:初始态
    public final static int CONNECT_SUCCESS = 1; // 1:连接成功
    public final static int CONNECT_DISCONNECT = 2; // 2:断线
    public final static int CONNECT_RECONNECTION = 3; // 3:正在重连

    /**
     * 连接状态
     */
    private static transient int connectStatus = 0;
    /**
     * 心跳回复时间
     */
    private static Long heartTime = new Date().getTime();
    /**
     * 网关编号
     */
    private static String gatewayId = "";

    public static int getConnectStatus() {
        return connectStatus;
    }

    public static void setConnectStatus(int connectStatus) {
        PlatformConnectStatus.connectStatus = connectStatus;
    }

    public static Long getHeartTime() {
        return heartTime;
    }

    public static void setHeartTime(Long heartTime) {
        PlatformConnectStatus.heartTime = heartTime;
    }

    public static String getGatewayId() {
        return gatewayId;
    }

    public static void setGatewayId(String gatewayId) {
        PlatformConnectStatus.gatewayId = gatewayId;
    }
}
