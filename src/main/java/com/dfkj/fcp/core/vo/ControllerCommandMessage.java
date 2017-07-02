package com.dfkj.fcp.core.vo;

import com.dfkj.fcp.core.constant.EAction;
import com.dfkj.fcp.core.constant.EDeviceType;
import com.dfkj.fcp.core.constant.EMessageType;
import com.dfkj.fcp.core.constant.EPriorityLvl;
import com.dfkj.fcp.core.util.FormatUtil;

/**
 * 控制器的控制命令message
 * @author songfei
 * @date 2016-06-01
 *
 */
public class ControllerCommandMessage extends Message {

	private String deviceId; 		// 设备ID
	private int channelNo; 			// 通道号
	private EAction action;			//	执行动作
	private EDeviceType devType;	//	设备类型

	/* 控制参数 */
	private double paramVector;	//	控制的向量
	private double paramSum;		//	控制的总量
	private double paramRate;	//	控制的速率
	private int paramTime;		//	控制的时间(单位秒)

	public ControllerCommandMessage() {
		super(EMessageType.CTRL_ACTION);
		this.ptyLvl = EPriorityLvl.HIGH;
	}
	
	public static ControllerCommandMessage create(Message message) {
		ControllerCommandMessage ctrlMessage = new ControllerCommandMessage();
		
		ctrlMessage.setSequence(message.getSequence());
		ctrlMessage.setAck((int)message.getAck());
		ctrlMessage.setId(message.getId());
		ctrlMessage.setDeviceNo(message.getDeviceNo());
		ctrlMessage.setOrgData(message.getOrgData());
		ctrlMessage.setRecvMsgDate(message.getRecvMsgDate());
		ctrlMessage.setMsgType(message.getMsgType());
		ctrlMessage.setDevCategory(message.getDevCategory());
		ctrlMessage.setPtyLvl(EPriorityLvl.HIGH);
		ctrlMessage.setVersion(message.getVersion());
		
		ctrlMessage.setDeviceId(message.getDeviceNo());
		ctrlMessage.setAction(EAction.NO_ACTION);
		
		return ctrlMessage;
	}
	
	@Override
	public String toString() {
		String actParamString = String.format("V=%s,S=%s,R=%s,T=%s秒",
				FormatUtil.DEC_FORMAT.format(this.getParamVector()),
				FormatUtil.DEC_FORMAT.format(this.getParamSum()),
				FormatUtil.DEC_FORMAT.format(this.getParamRate()),
				FormatUtil.DEC_FORMAT.format(this.getParamTime())
				);
		
		return String.format("设备ID:%s 版本:%d 消息类型:%s 设备类型:%s 通道号:%d 动作:%s 动作参数[%s]  收到时间:%s", 
				this.getDeviceId(), getVersion(), this.getMsgType(), this.getDevType(), this.getChannelNo(), this.getAction(),
				actParamString,
				getRecvMsgDate() == null ? "?" : FormatUtil.DATE_FORMAT.format(getRecvMsgDate()));
	}

	@Override
	public int hashCode() {
		int did = Integer.parseInt(deviceId.replaceAll("-", ""), 16);
		String value = String.format("%d%d%s", did, channelNo, devType.toString());
		int hash = value.hashCode();//Integer.parseInt(value);
        return hash;
	}
	
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public int getChannelNo() {
		return channelNo;
	}

	public void setChannelNo(int channelNo) {
		this.channelNo = channelNo;
	}

	public EAction getAction() {
		return action;
	}

	public void setAction(EAction action) {
		this.action = action;
	}

	public double getParamVector() {
		return paramVector;
	}

	public void setParamVector(double paramVector) {
		this.paramVector = paramVector;
	}

	public double getParamSum() {
		return paramSum;
	}

	public void setParamSum(double paramSum) {
		this.paramSum = paramSum;
	}

	public double getParamRate() {
		return paramRate;
	}

	public void setParamRate(double paramRate) {
		this.paramRate = paramRate;
	}

	public int getParamTime() {
		return paramTime;
	}

	public void setParamTime(int paramTime) {
		this.paramTime = paramTime;
	}

	public EDeviceType getDevType() {
		return devType;
	}

	public void setDevType(EDeviceType devType) {
		this.devType = devType;
	}
	
	
}
