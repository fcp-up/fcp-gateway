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
public class GatewayParseImpl implements IParseGtw1P1 {
	
	private final static AcpLogger logger = new AcpLogger(GatewayParseImpl.class);
	public Message parse(Message message, ByteArray content) {
		if (message.getMsgType() != EMessageType.SENSOR_DATA) {
			logger.debug("传感器设备，暂只支持数据消息.");
			return message;
		}
		//解析传感器数据		
        logger.debug("SensorParseImpl:\n" + message);        
		return message;
	}

	public ByteArray unparse(Message message) {
		return null;
	}
	

}
