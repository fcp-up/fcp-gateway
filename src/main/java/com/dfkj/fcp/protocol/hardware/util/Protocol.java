/**
 * 
 */
package com.dfkj.fcp.protocol.hardware.util;

import com.dfkj.fcp.core.constant.EDeviceCategory;
import com.dfkj.fcp.core.constant.EMessageType;
import com.dfkj.fcp.core.constant.EUniversalResponseCode;
import com.dfkj.fcp.core.util.ByteArray;
import com.dfkj.fcp.core.vo.ControllerCommandMessage;
import com.dfkj.fcp.core.vo.Message;
import com.dfkj.fcp.core.vo.UniversalResponseMessage;

/**
 * @author Administrator
 *
 */
public interface Protocol {	
	public Message unpack(ByteArray byteArray);
	public ByteArray pack(Message message);
	public Message parse(Message message, ByteArray content);
	public EMessageType EMessageTypeFactory(short msgId);
	public EDeviceCategory EDeviceCategoryFactory(EMessageType msgType);
	public ByteArray createUniversalResponse(UniversalResponseMessage message);
	public ByteArray createUniversalResponseBody(UniversalResponseMessage message, EUniversalResponseCode code);
	public ByteArray createCtrlActionMessage(ControllerCommandMessage actionMessage);
	public ByteArray createCtrlActionMessageBody(ControllerCommandMessage actionMessage);
	public ByteArray createGatewayHeartbeat(Message message);
	public boolean isCurrentProtocol(ByteArray byteArray);
}
