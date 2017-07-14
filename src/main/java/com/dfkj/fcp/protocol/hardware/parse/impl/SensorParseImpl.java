package com.dfkj.fcp.protocol.hardware.parse.impl;

import com.dfkj.fcp.core.constant.EMessageType;
import com.dfkj.fcp.core.logger.AcpLogger;
import com.dfkj.fcp.core.util.ByteArray;
import com.dfkj.fcp.core.util.PlusList;
import com.dfkj.fcp.core.vo.Message;
import com.dfkj.fcp.protocol.hardware.parse.DataItemParseManage;
import com.dfkj.fcp.protocol.hardware.parse.IParseGtw1P1;

/**
 * 传感器消息体解析
 * @author Administrator
 *
 */
public class SensorParseImpl implements IParseGtw1P1 {
	
	private final static AcpLogger logger = new AcpLogger(SensorParseImpl.class);
	public Message parse(Message message, ByteArray content) {
		if (message.getMsgType() != EMessageType.SENSOR_DATA) {
			logger.debug("传感器设备，暂只支持数据消息.");
			return message;
		}		
		//解析传感器数据	
		//解析设备ID
		message.setDeviceId(content.getAt(0)<<8 & 0xFF00 | content.getAt(1) & 0x00FF);		
		content.removeAt(0,2);
		//解析节点信号强度
		message.setDeviceSignal(content.getAt(0)&0x0F);
		content.removeAt(0);
		//解析报警标识
		message.setIsAlarm(content.getAt(0)&0x0F);
		content.removeAt(0,1);
		//解析报警数值
		message.setCurrentValue(content.getAt(0)<<8 & 0xFF00 | content.getAt(1) & 0x00FF);
		content.removeAt(0,2);
		//解析电池电压
		float voltage = content.getAt(0)&0xFF;
		message.setVoltage(voltage / 10);
		content.removeAt(0,1);
        logger.debug("GatewayParseImpl:\n" + message);        
		return message;
	}

	public ByteArray unparse(Message message) {
		return null;
	}
	

}
