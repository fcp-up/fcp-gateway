package com.dfkj.fcp.core.vo;

import java.io.Serializable;
import java.util.Date;

import com.dfkj.fcp.core.constant.EFrameType;
import com.dfkj.fcp.core.util.FormatUtil;

public class AlarmMessage implements Cloneable,Serializable {
	protected String centerNo;	//通讯设备编号(集中器编号) dflr000000000001 
	protected String deviceNo;  //节点编号(探头编号) 225
	protected int isAlarm; //是否报警 1 是  0 否
	protected double voltage;//当前的电压
	protected EFrameType frameType; //报文类型
	protected String orgData; //报文字符串形式
	protected Date recvMsgDate; // 收到message时的时间
	
	public AlarmMessage(String orgData) {
		this.orgData = orgData;
	}
	
	public AlarmMessage(EFrameType frameType) {
		this.frameType = frameType;
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
		String value = deviceNo.replaceAll("-", "");
		int hash = Integer.parseInt(value);
        return hash;
	}
	
	@Override
	public boolean equals(Object o) {
		return (this.hashCode() == o.hashCode());
	}
	
	@Override
	public String toString() {
		return String.format("通讯设备编号:%s 设备编号:%s 是否报警:%d 当前电压:%s 报文类型:%s", 
				this.centerNo, this.deviceNo, this.isAlarm,
				this.voltage, this.frameType, 
				recvMsgDate == null ? "?" : FormatUtil.DATE_FORMAT.format(recvMsgDate));
	}
	
	public String getCenterNo() {
		return centerNo;
	}
	public void setCenterNo(String centerNo) {
		this.centerNo = centerNo;
	}
	
	public String getDeviceNo() {
		return deviceNo;
	}
	
	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}
	
	public int getIsAlarm() {
		return isAlarm;
	}
	
	public void setIsAlarm(int isAlarm) {
		this.isAlarm = isAlarm;
	}
	
	public double getVoltage() {
		return voltage;
	}
	
	public void setVoltage(double voltage) {
		this.voltage = voltage;
	}
	
	public EFrameType getFrameType() {
		return frameType;
	}
	
	public void setFrameType(EFrameType frameType) {
		this.frameType = frameType;
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
	
}
