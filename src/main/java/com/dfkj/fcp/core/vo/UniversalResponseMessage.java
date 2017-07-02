package com.dfkj.fcp.core.vo;

import com.dfkj.fcp.core.constant.EMessageType;
import com.dfkj.fcp.core.constant.EPriorityLvl;
import com.dfkj.fcp.core.constant.EUniversalResponseCode;
import com.dfkj.fcp.core.util.FormatUtil;

import java.util.Date;

/**
 * 通用回复Message
 * @author songfei
 * @date 2016-05-30
 *
 */
public class UniversalResponseMessage extends Message {
	private int respMsgSequence;	//	回复对应的message流水号
	private EUniversalResponseCode respCode;	//	回复状态
	private EMessageType respMsgType;	//	回复对应的MessageType
	private Date respDate;	//	回复时间
	private String msg;		//	如果有回复的消息，填入此项

	public UniversalResponseMessage() {
		super(EMessageType.UNIVERSAL_RESPONSE);
	}
	
	public static UniversalResponseMessage create(Message message, EUniversalResponseCode code) {

		UniversalResponseMessage response = new UniversalResponseMessage();
		
		response.setRespMsgSequence(message.getSequence());
		response.setRespCode(code);
		response.setRespDate(new Date());
		response.setRespMsgType(EMessageType.UNKNOWN);
		
		response.setDeviceNo(message.getDeviceNo());
		response.setAck(1);
		response.setId(message.getId());
		response.setRecvMsgDate(response.getRespDate());
		response.setPtyLvl(EPriorityLvl.LOW);
		response.setMsgType(message.getMsgType());
		response.setDevCategory(message.getDevCategory());
		response.setVersion(message.getVersion());
		
		return response;
	}
	
	@Override
	public String toString() {
		return String.format("设备号:%s 版本号:%d 流水号:%d 设备类别:%s 消息类型:%s 优先级:%s 包方向:%s 回复流水号:%d 回复代码:%s 回复时间:%s 接收时间:%s 回复消息:%s", 
				this.getDeviceNo(), this.getVersion(), this.getSequence(),
				this.getDevCategory(), this.getMsgType(), this.getPtyLvl(),
				this.getAck() < 0 ? "请求" : "应答",
				this.getRespMsgSequence(), this.getRespCode(),
				getRespDate() == null ? "?" : FormatUtil.DATE_FORMAT.format(getRespDate()),
				getRecvMsgDate() == null ? "?" : FormatUtil.DATE_FORMAT.format(getRecvMsgDate()),
				getMsg() == null ? "" : getMsg()
				);
	}
	
	public int getRespMsgSequence() {
		return respMsgSequence;
	}
	public void setRespMsgSequence(int respMsgSequence) {
		this.respMsgSequence = respMsgSequence;
	}
	public EUniversalResponseCode getRespCode() {
		return respCode;
	}
	public void setRespCode(EUniversalResponseCode respCode) {
		this.respCode = respCode;
	}
	public Date getRespDate() {
		return respDate;
	}
	public void setRespDate(Date respDate) {
		this.respDate = respDate;
	}
	public EMessageType getRespMsgType() {
		return respMsgType;
	}
	public void setRespMsgType(EMessageType respMsgType) {
		this.respMsgType = respMsgType;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
