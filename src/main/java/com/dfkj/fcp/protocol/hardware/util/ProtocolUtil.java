package com.dfkj.fcp.protocol.hardware.util;

import java.util.Date;

import com.dfkj.fcp.core.constant.EDeviceCategory;
import com.dfkj.fcp.core.constant.EMessageType;
import com.dfkj.fcp.core.logger.AcpLogger;
import com.dfkj.fcp.core.util.ByteArray;
import com.dfkj.fcp.core.util.HexUtil;
import com.dfkj.fcp.core.vo.Message;
import com.dfkj.fcp.protocol.hardware.parse.IParseGtw1P1;
import com.dfkj.fcp.protocol.hardware.parse.impl.SensorParseImpl;

public class ProtocolUtil {

	private final static AcpLogger logger = new AcpLogger(ProtocolUtil.class);
	
	/**
	 * 解包数据
	 * 
	 * @param byteArray
	 * @return
	 */
	public static Message unpack(ByteArray byteArray) {
		Message message = null;

		do {
			if (byteArray.getBeginByte() != (byte) 0x7E || byteArray.getLastByte() != (byte) 0x7E) {
				logger.debug("无效的数据包.");
				break;
			}

			byteArray.removeBeginByte().removeLastByte(); // 掐头去尾
			byteArray.replace(new byte[] { 0x7D, 0x02 }, new byte[] { 0x7E }).replace(new byte[] { 0x7D, 0x01 },
					new byte[] { 0x7D }); // 反转义

			// 因校验规则为异或，所以连同校验位不为0则数据不正确
			
			byteArray.removeLastByte();

			message = new Message(HexUtil.ByteToString(byteArray.toBytes(), " "));

			
			//logger.debug(String.format("消息标识[%2X].", byteArray.getAt(8)));
			
			// 消息类型
			message.setMsgType(EMessageTypeFactory(byteArray.getShortAt(0)));
			
			//解决控制器上报问题 2016-09-19 mhz
			if(byteArray.getAt(8) == 0x40){
				message.setMsgType(EMessageType.CTRL_REPORT_STATUS);
			}
			
			byteArray.removeAt(0, 2);
			
		

			// TODO 暂不做消息长度的验证
			short messageLength = byteArray.getShortAt(0);
			byteArray.removeAt(0, 2);



			// 消息时间
			message.setRecvMsgDate(new Date());

			// 设备类别
			message.setDevCategory(EDeviceCategoryFactory(message.getMsgType()));

		} while (false);

		return message;
	}

	public static ByteArray pack(Message message) {
		
		ByteArray byteArray = null;		
		return byteArray;
	}

	public static Message parse(Message message, ByteArray content) {
		// TODO 简单的进行处理，后期还需要重构
		Message retMessage = message;
		IParseGtw1P1 parse = null;
		if (message.getDevCategory() == EDeviceCategory.SENSOR) {
			parse = new SensorParseImpl();
		}

		if (parse != null) {
			retMessage = parse.parse(retMessage, content);
		}

		return retMessage;
	}

	public static EMessageType EMessageTypeFactory(short msgId) {
		int value = (int)(msgId & 0xFFFF);
		
		EMessageType tp = null;
		switch (value) {
			case 0x0000: // 控制器设备的心跳
				tp = EMessageType.CTRL_REPORT_STATUS;
				break;
			case 0x0002:
				tp = EMessageType.HEARTBEAT;
				break;
			case 0x200:
				tp = EMessageType.SENSOR_DATA;
				break;
			case 0xFFF0:
				tp = EMessageType.GATEWAY_HEARTBEAT;
				break;
			case 0x8001:
				tp = EMessageType.UNIVERSAL_RESPONSE;
				break;
			default:
				tp = EMessageType.UNKNOWN;
				break;
		}
		return tp;
	}

	/**
	 * 从MsgType推断出设备的类别（通过协议中的msgid来判断设备的类别）
	 * 
	 * @param msgType
	 * @return
	 */
	public static EDeviceCategory EDeviceCategoryFactory(EMessageType msgType) {
		EDeviceCategory category = EDeviceCategory.COMMON;
		if (msgType == EMessageType.HEARTBEAT) {
			category = EDeviceCategory.COMMON;
		} else if (msgType == EMessageType.SENSOR_DATA) {
			category = EDeviceCategory.SENSOR;
		} else if (msgType == EMessageType.CTRL_REPORT_STATUS) {
			category = EDeviceCategory.CONTROLLER;
		} else if (msgType == EMessageType.GATEWAY_HEARTBEAT) {
			category = EDeviceCategory.GATEWAY;
		}
		return category;
	}
	
	/**
	 * 网关心跳
	 * 在此协议中没有规定，但是为了模拟出网关而添加的
	 * @param message
	 * @return
	 */
	public static ByteArray createGatewayHeartbeat(Message message) {
		ByteArray content = new ByteArray();		

		return content;
	}
}
