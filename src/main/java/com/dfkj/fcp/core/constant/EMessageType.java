package com.dfkj.fcp.core.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息类型
 * @author songfei
 *
 */
public enum EMessageType {
	UNKNOWN(-1),		//	未知
	HEARTBEAT(0),		//	心跳消息
	HELLO(1),			//握手信号	
	SENSOR_DATA(2);		//	传感器采集数据	
	private final static Map<Integer, String> descriptionMap = new HashMap<Integer, String>() {
		private static final long serialVersionUID = 1L;
		{
			put(-1, "未知消息");
			put(0, "心跳消息");
			put(1, "握手信号");
			put(2, "传感器数据");
		}
	};
	
	private int msgType;

	private EMessageType(int tp) {
		this.msgType = tp;
	}
	
	@Override
	public String toString() {
		return descriptionMap.get(msgType);
	}
}
