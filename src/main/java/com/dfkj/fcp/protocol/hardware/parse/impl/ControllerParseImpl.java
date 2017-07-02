package com.dfkj.fcp.protocol.hardware.parse.impl;

import com.dfkj.fcp.core.constant.EDeviceStatus;
import com.dfkj.fcp.core.constant.EDeviceType;
import com.dfkj.fcp.core.constant.EMessageType;
import com.dfkj.fcp.core.logger.AcpLogger;
import com.dfkj.fcp.core.util.ByteArray;
import com.dfkj.fcp.core.util.PlusList;
import com.dfkj.fcp.core.vo.ControllerMessage;
import com.dfkj.fcp.core.vo.ControllerMessageItem;
import com.dfkj.fcp.core.vo.Message;
import com.dfkj.fcp.protocol.hardware.parse.IParseGtw1P1;

import java.util.Date;

/**
 * 传感器类消息解析
 *
 * @author Administrator
 *
 */
public class ControllerParseImpl implements IParseGtw1P1 {

	private final static AcpLogger logger = new AcpLogger(ControllerParseImpl.class);
	
	public Message parse(Message message, ByteArray content) {
		if (message.getMsgType() != EMessageType.CTRL_REPORT_STATUS) {
			logger.debug("控制器设备，暂只支持汇报状态信息.");
			return message;
		}
		
		ControllerMessage ctrlMessage = ControllerMessage.create(message);
		PlusList<ControllerMessageItem> items = new PlusList<ControllerMessageItem>();
		
		content.removeAt(0, 2);//2016-09-19 mhz
		while (content.size() != 0) {
			content.removeBeginByte();//2016-09-19 mhz
			int channelNo = content.getBeginByte();	//	通道号
			content.removeBeginByte();
			
			int runStatus = content.getBeginByte();	//	运行状态
			content.removeBeginByte();
			
			ControllerMessageItem item = new ControllerMessageItem();
			
			item.setDeviceId(message.getDeviceNo());
			item.setChannelNo(channelNo);
			item.setDevStatus(EDeviceStatus.NORMAL);

			//	TODO 目前只有触发开关
			item.setDevType(EDeviceType.TRIGGER_SWITCH);
			
			item.setRunStatus(EDeviceStatusFactory(runStatus));
			item.setDate(new Date());
			
			items.add(item);
		}
		
		ctrlMessage.setItems(items);
		
		logger.debug("ControllerParseImpl:\n" + ctrlMessage);
		
		return ctrlMessage;
	}

	private static EDeviceStatus EDeviceStatusFactory(int status) {
		EDeviceStatus runStatus = EDeviceStatus.CLOSE;
		switch (status) {
			case 3:
			default:
			case 0:
				runStatus = EDeviceStatus.CLOSE;
				break;
			case 1:
			case 2:
				runStatus = EDeviceStatus.OPEN;
				break;
		}
		return runStatus;
	}
}
