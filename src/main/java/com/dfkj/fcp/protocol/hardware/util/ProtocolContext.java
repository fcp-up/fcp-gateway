package com.dfkj.fcp.protocol.hardware.util;

import com.dfkj.fcp.core.constant.EDeviceCategory;
import com.dfkj.fcp.core.constant.EMessageType;
import com.dfkj.fcp.core.constant.EUniversalResponseCode;
import com.dfkj.fcp.core.logger.AcpLogger;
import com.dfkj.fcp.core.util.ByteArray;
import com.dfkj.fcp.core.vo.ControllerCommandMessage;
import com.dfkj.fcp.core.vo.Message;
import com.dfkj.fcp.core.vo.UniversalResponseMessage;

public class ProtocolContext {	
	
   private final static AcpLogger logger = new AcpLogger(ProtocolContext.class);	
   Protocol protocol;
   public int protocolTag;
   
   public ProtocolContext(Protocol protocol) {	   
	   this.protocol = protocol;
   }
   
   public Message unpack(ByteArray byteArray) {
	  return protocol.unpack(byteArray);
   }
   
   public ByteArray pack(Message message) {
	   return protocol.pack(message);
   }
   
   public Message parse(Message message, ByteArray content){
	   return protocol.parse(message, content);
   }
   
   public EMessageType EMessageTypeFactory(short msgId){
	   return protocol.EMessageTypeFactory(msgId);
   }
   
   public  EDeviceCategory EDeviceCategoryFactory(EMessageType msgType){
	   return protocol.EDeviceCategoryFactory(msgType);
   }
   
   public ByteArray createUniversalResponse(UniversalResponseMessage message){
	   return protocol.createUniversalResponse(message);
   }
   
   public ByteArray createUniversalResponseBody(UniversalResponseMessage message,EUniversalResponseCode code){
	   return protocol.createUniversalResponseBody(message,code);
   }
   
   public ByteArray createCtrlActionMessage(ControllerCommandMessage actionMessage){
	   return protocol.createCtrlActionMessage(actionMessage);
   }
   
   public ByteArray createCtrlActionMessageBody(ControllerCommandMessage actionMessage){
	   return protocol.createCtrlActionMessageBody(actionMessage);
   }
   
  public  ByteArray createGatewayHeartbeat(Message message){
	  return protocol.createGatewayHeartbeat(message); 
  }
}
