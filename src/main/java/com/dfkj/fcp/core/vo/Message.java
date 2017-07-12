package com.dfkj.fcp.core.vo;

import com.dfkj.fcp.core.constant.EDeviceCategory;
import com.dfkj.fcp.core.constant.EMessageType;
import com.dfkj.fcp.core.constant.EPriorityLvl;
import com.dfkj.fcp.core.util.FormatUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息类
 */
public class Message implements Cloneable,Serializable {
	
	protected String centerNo; //集中器ID
	protected int centerSignal;//集中器信号强度
	protected int deviceId; //设备ID
	protected int deviceSignal;//节点信号强度
	protected EDeviceCategory devCategory; // 设备类别
	protected int isAlarm; //是否报警
	protected float currentValue;//当前值
	protected float voltage;//电池电压
	protected String orgData;//数据体
	protected Date recvMsgDate; // 收到message时的时间
	protected EMessageType msgType; // 消息类型
	
	public String getCenterNo() {
		return centerNo;
	}
	
	public void setCenterNo(String centerNo) {
		this.centerNo = centerNo;
	}	
	
	public int getCenterSignal() {
		return centerSignal;
	}

	public void setCenterSignal(int centerSignal) {
		this.centerSignal = centerSignal;
	}

	public int getDeviceId() {
		return deviceId;
	}
	
	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}	
	
	public int getDeviceSignal() {
		return deviceSignal;
	}

	public void setDeviceSignal(int deviceSignal) {
		this.deviceSignal = deviceSignal;
	}

	public EDeviceCategory getDevCategory() {
		return devCategory;
	}
	
	public void setDevCategory(EDeviceCategory devCategory) {
		this.devCategory = devCategory;
	}
	
	public int getIsAlarm() {
		return isAlarm;
	}
	
	public void setIsAlarm(int isAlarm) {
		this.isAlarm = isAlarm;
	}
	
	public float getCurrentValue() {
		return currentValue;
	}
	
	public void setCurrentValue(float currentValue) {
		this.currentValue = currentValue;
	}		
	
	public float getVoltage() {
		return voltage;
	}

	public void setVoltage(float voltage) {
		this.voltage = voltage;
	}

	public String getOrgData() {
		return orgData;
	}
	
	public void setOrgData(String orgData) {
		this.orgData = orgData;
	}
	
	public Date getRecvMsgDate() {
		return recvMsgDate;
	}
	
	public void setRecvMsgDate(Date recvMsgDate) {
		this.recvMsgDate = recvMsgDate;
	}
	
	public EMessageType getMsgType() {
		return msgType;
	}
	
	public void setMsgType(EMessageType msgType) {
		this.msgType = msgType;
	}
	
	public Message(String orgData) {
		this.orgData = orgData;
	}
	
	public Message(EMessageType messageType) {
		this.msgType = messageType;
	}
	
	public Object clone() {
		Object o = null;
		try {
			o = super.clone();
		}
		catch(CloneNotSupportedException e) {
			o = null;
		}
		return o;
	}

	@Override
	public int hashCode() {
		String value = centerNo + deviceId;
		int hash = Integer.parseInt(value);
        return hash;
	}
	
	@Override
	public boolean equals(Object o) {
		return (this.hashCode() == o.hashCode());
	}	
	
	@Override
	public String toString() {
		return String.format("集中器编号:%s 集中器信号强度:%d 设备类别:%s 消息类型:%s 接收时间:%s 设备ID:%d 节点信号强度:%d 是否报警:%d 当前数值:%.1f 当前电压:%.1f", 
				this.centerNo,this.centerSignal,this.devCategory, this.msgType, 
				recvMsgDate == null ? "?" : FormatUtil.DATE_FORMAT.format(recvMsgDate),
							this.deviceId,this.deviceSignal,this.isAlarm,this.currentValue,this.voltage);
	}
	

}
