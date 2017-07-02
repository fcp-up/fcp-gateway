package com.dfkj.fcp.core.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 设备状态
 * @author songfei
 *
 */
public enum EDeviceStatus {

	NORMAL(0), WARNNING(1), ACCESS(2), NO_ACCESS(3), TROUBLE(4),
	
	/* 控制器设备状态 */
	OPEN(301),  OPENING(302), STOP(303), CLOSING(304), CLOSE(305), RUNNING(306);
	
	private final static Map<Integer, String> descriptionMap = new HashMap<Integer, String>() {
		private static final long serialVersionUID = 1L;

		{
			put(0, "0");//正常
			put(1, "1");//发现险情
			put(2, "接入");
			put(3, "未接入");
			put(4, "故障");
			
			/* 控制器设备状态 */
			put(301, "打开");
			put(302, "正在打开中");
			put(303, "停止");
			put(304, "正在关闭中");
			put(305, "关闭");
			put(306, "正在运行");
		}
	};
	
	private int status;

	private EDeviceStatus(int st) {
		this.status = st;
	}
	
	@Override
	public String toString() {
		return descriptionMap.get(status);
	}
}
