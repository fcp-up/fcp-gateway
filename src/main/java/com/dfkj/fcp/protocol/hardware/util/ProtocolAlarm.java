package com.dfkj.fcp.protocol.hardware.util;

import com.dfkj.fcp.core.constant.EDeviceCategory;
import com.dfkj.fcp.core.constant.EMessageType;
import com.dfkj.fcp.core.constant.EUniversalResponseCode;
import com.dfkj.fcp.core.logger.AcpLogger;
import com.dfkj.fcp.core.util.ByteArray;
import com.dfkj.fcp.core.util.HexUtil;
import com.dfkj.fcp.core.vo.ControllerCommandMessage;
import com.dfkj.fcp.core.vo.Message;
import com.dfkj.fcp.core.vo.UniversalResponseMessage;
import com.dfkj.fcp.protocol.hardware.parse.IParseGtw1P1;
import com.dfkj.fcp.protocol.hardware.parse.impl.AlarmParseImpl;

public class ProtocolAlarm implements Protocol {
	
	private final static AcpLogger logger = new AcpLogger(ProtocolAlarm.class);

	@Override
	public Message unpack(ByteArray byteArray) {
		// TODO Auto-generated method stub
		Message message = null;
		message = new Message(HexUtil.ByteToString(byteArray.toBytes(), " "));
		do {
			
			if(this.isHello(byteArray)){
				message.setAck(-1);
				message.setMsgType(EMessageType.HELLO);
			}
			if(this.isHeartBeat(byteArray)){
				message.setAck(1);
				message.setMsgType(EMessageType.HEARTBEAT);
			}
			if(this.isData(byteArray)){
				message.setAck(2);
				message.setMsgType(EMessageType.SENSOR_DATA);				
			}
			message.setDevCategory(EDeviceCategory.DETECTOR);			

		} while (false);
		
		return message;
	}

	@Override
	public ByteArray pack(Message message) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Message parse(Message message, ByteArray content) {
		// TODO Auto-generated method stub
		Message retMessage = message;
		IParseGtw1P1 parse = null;
		parse = new AlarmParseImpl();
		retMessage = parse.parse(retMessage, content);
		return retMessage;
	}

	@Override
	public EMessageType EMessageTypeFactory(short msgId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EDeviceCategory EDeviceCategoryFactory(EMessageType msgType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ByteArray createUniversalResponse(UniversalResponseMessage message) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ByteArray createUniversalResponseBody(UniversalResponseMessage message, EUniversalResponseCode code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ByteArray createCtrlActionMessage(ControllerCommandMessage actionMessage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ByteArray createCtrlActionMessageBody(ControllerCommandMessage actionMessage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ByteArray createGatewayHeartbeat(Message message) {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected boolean isHello(ByteArray byteArray){
	    if(byteArray.size()>=16 && byteArray.size()<=18){
			return true;
		}
		return false;
	}
	
	protected boolean isHeartBeat(ByteArray byteArray){
		if(byteArray.size()==3){
			return true;
		}
		return false;
	}
	
	protected boolean isData(ByteArray byteArray){
		if(byteArray.size()==10){
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isCurrentProtocol(ByteArray byteArray){
		if(isData(byteArray)||isHeartBeat(byteArray)||isHello(byteArray)){
			return true;
		}
		return false;
	}

}
