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
	UNIVERSAL_RESPONSE(1),	//	通用回复
	GATEWAY_HEARTBEAT(100),	//	网关心跳
	SENSOR_DATA(200),		//	传感器采集数据
	CTRL_REPORT_STATUS(300),	//	控制器汇报状态
	CTRL_ACTION(301),		//	控制器执行动作
	CTRL_ACTION_RESP(302), //控制器执行动作回复
	HELLO(2); //握手信号

	private final static Map<Integer, String> descriptionMap = new HashMap<Integer, String>() {
		private static final long serialVersionUID = 1L;

		{
			put(-1, "未知消息");
			put(0, "心跳消息");
			put(1, "通用回复");
			put(100, "网关心跳");
			put(200, "探测器数据");
			put(300, "控制器汇报状态数据");
			put(301, "控制器执行动作命令");
			put(302, "控制器执行动作回复命令");
			put(2,"握手信号");
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
