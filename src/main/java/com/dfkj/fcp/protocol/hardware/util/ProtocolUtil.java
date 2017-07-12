package com.dfkj.fcp.protocol.hardware.util;

import java.util.Date;

import com.dfkj.fcp.core.constant.EDeviceCategory;
import com.dfkj.fcp.core.constant.EMessageType;
import com.dfkj.fcp.core.logger.AcpLogger;
import com.dfkj.fcp.core.util.ByteArray;
import com.dfkj.fcp.core.util.CRC16;
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
			if (isNotData(byteArray)&&isNotHeartBeat(byteArray)) {
				logger.debug("无效的数据包.");
				break;
			}
			byteArray.removeBeginByte().removeLastByte(); // 掐头去尾
			//CRC校验	
			int crc = (int)CRC16.calcCrc16(byteArray.subByteArray(byteArray,6,14));
			int currentCRC = (int)byteArray.getShortAt(15);
			if(crc != currentCRC){
				logger.debug("数据包效验失败.");
				break;
			}
			//去掉校验码			
			byteArray.removeAt(byteArray.size() -2 , 2); 		    		
		    message = new Message(HexUtil.ByteToString(byteArray.toBytes(), " "));				
			//集中器地址
		    //String centerNoStr = new String(HexUtil.getChars(byteArray.subByteArray(byteArray,0,12)));	
		    int preCenterNo = (int)byteArray.getLongAt(0);
		    byteArray.removeAt(0,2);
		    int suffixCenterNo = (int)byteArray.getLongAt(0);		    
		    byteArray.removeAt(0,2);
		    String centerNoStr = autoNoStr(preCenterNo,5) + autoNoStr( suffixCenterNo,5);
		    message.setCenterNo(centerNoStr);			    
			//获取消息长度
			short msgLen = byteArray.getShortAt(0);
			byteArray.removeAt(0, 2);
			//集中器信号强度
		    short centerSignal = byteArray.getShortAt(0);
		    message.setCenterSignal(centerSignal);
			//消息类型
		    byte msgFlag = byteArray.getAt(1);
		    message.setMsgType(EMessageTypeFactory(msgFlag));				
			//消息时间
			message.setRecvMsgDate(new Date());
			//设备类别
			message.setDevCategory(EDeviceCategoryFactory(message.getMsgType()));			
			//解析传感器数据
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
		int value = (int)(msgId & 0xFF);		
		EMessageType tp = null;
		switch (value) {
			case 0x00:
				tp = EMessageType.HELLO;
				break;
			case 0x01:
				tp = EMessageType.SENSOR_DATA;
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
		EDeviceCategory category = EDeviceCategory.UNKNOWN;		
		if (msgType == EMessageType.HELLO) {
			category = EDeviceCategory.GATEWAY;
		} else if (msgType == EMessageType.SENSOR_DATA) {
			category = EDeviceCategory.SENSOR;
		} else if (msgType == EMessageType.HEARTBEAT) {
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
	
	/**
	 * 判断是否是数据包(节点数据或终端握手数据)
	 * @param content
	 * @return
	 */
	public static boolean  isNotData(ByteArray content){
		boolean flag = true;
		if((content.getBeginByte() == 0x7E 
				|| content.getLastByte() == 0x7E)){
			flag = false;
		}
		return flag;
	}	
	
	public static boolean isNotHeartBeat(ByteArray content){
		boolean flag = true;
		if(content.getLongAt(0) == 0){
			flag = false;
		}
		return flag;
	}
	
	/**
     * 不够位数的在前面补0，保留num的长度位数字
     * @param code
     * @return
     */
    private static String autoNoStr(int code, int num) {
        String result = "";
        result = String.format("%0" + num + "d", code);
        return result;
    }
}
