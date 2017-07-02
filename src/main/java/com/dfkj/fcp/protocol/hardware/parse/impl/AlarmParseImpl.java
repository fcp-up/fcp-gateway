package com.dfkj.fcp.protocol.hardware.parse.impl;

import java.util.Date;

import com.dfkj.fcp.core.constant.EDeviceStatus;
import com.dfkj.fcp.core.constant.EDeviceType;
import com.dfkj.fcp.core.constant.EMessageType;
import com.dfkj.fcp.core.constant.EValueStatus;
import com.dfkj.fcp.core.logger.AcpLogger;
import com.dfkj.fcp.core.util.ByteArray;
import com.dfkj.fcp.core.vo.DataMessageItem;
import com.dfkj.fcp.core.vo.Message;
import com.dfkj.fcp.protocol.hardware.parse.IParseGtw1P1;



public class AlarmParseImpl implements IParseGtw1P1 {
	
	private final static AcpLogger logger = new AcpLogger(AlarmParseImpl.class);

	@Override
	public Message parse(Message message, ByteArray content) {
		// TODO Auto-generated method stub
		if (message.getMsgType() != EMessageType.SENSOR_DATA) {
			logger.debug("传感器设备，暂只支持数据消息.");
			return message;	
		}		
		content.removeAt(0, 4);	//	移除数据头		
		int deviceNo = (int)(content.getShortAt(0) & 0xFFFF); //获取设备ID
		content.removeAt(0, 2);	
		short isAlarm = (short)(content.getBeginByte());//获取报警值
		content.removeAt(0, 1);	
		short isData = (short)(content.getBeginByte());//获取电池电压	
		message.setDeviceNo(Integer.toString(deviceNo));
		message.setRecvMsgDate(new Date());
		message.setAck(-2);
		
		DataMessageItem dataItem = new DataMessageItem() ;
		dataItem.setDeviceId(Integer.toString(deviceNo));
		dataItem.setDate(message.getRecvMsgDate());
		dataItem.setDevStatus(isAlarm == 1 ? EDeviceStatus.WARNNING : EDeviceStatus.NORMAL);
		dataItem.setValue((short)isData/10);	
		dataItem.setValStatus(EValueStatus.VALID);
		dataItem.setChannelNo(deviceNo);
		dataItem.setDeviceId("sssss");
		dataItem.setDevType(EDeviceType.DETECTOR);
		
		
		logger.debug("data:\n" + dataItem.toString());
		logger.debug("msg:\n" + message.toString());
		return message;
		
	}

}
