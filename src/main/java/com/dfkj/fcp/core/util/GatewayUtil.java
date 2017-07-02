package com.dfkj.fcp.core.util;

/**
 * 网管工具类
 */
public final class GatewayUtil {
	public static String getUUID() {
		return EnvVarUtil.getInstance().get("GATEWAY_ID");
	}
}
