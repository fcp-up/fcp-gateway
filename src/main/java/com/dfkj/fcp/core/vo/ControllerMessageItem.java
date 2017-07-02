package com.dfkj.fcp.core.vo;

import com.dfkj.fcp.core.constant.EDeviceStatus;
import com.dfkj.fcp.core.constant.EDeviceType;
import com.dfkj.fcp.core.util.FormatUtil;

import java.util.Date;

/**
 * 控制器Message中的item
 * @author songfei
 * @date 2016-06-01
 */
public class ControllerMessageItem implements Cloneable {

	private String deviceId; 		// 设备ID
	private int channelNo; 			// 通道号
	private EDeviceStatus devStatus;	//	设备状态[连接,未连接等]
	private EDeviceStatus runStatus;	//	设备运行状态
	private EDeviceType devType;
	private Date date;	//	汇报状态的时间
	
	/* 扩展字段 当前不使用 */
	private double curExtVector;	//	当前对控制指令执行的向量
	private double curExtSum;		//	当前对控制指令执行的总量
	private double curExtRate;	//	当前对控制指令执行的速率
	private int curExtTime;		//	当前对控制指令执行的时间(单位秒)
	
	@Override
	public String toString() {
		return String.format("设备ID:%s 设备类型:%s 通道号:%d 设备状态:%s 运行状态:%s 时间:%s", 
				this.getDeviceId(), this.getDevType(), this.getChannelNo(), this.getDevStatus(), this.getRunStatus(),
				this.getDate() == null ? "null" : FormatUtil.DATE_FORMAT.format(this.getDate()));
	}

	@Override
	public int hashCode() {
		int did = Integer.parseInt(getDeviceId().replaceAll("-", ""), 16);
		String value = String.format("%d%d%s", did, getChannelNo(), getDevType().toString());
		int hash = value.hashCode();//Integer.parseInt(value);
        return hash;
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

	public EDeviceStatus getDevStatus() {
		return devStatus;
	}

	public void setDevStatus(EDeviceStatus devStatus) {
		this.devStatus = devStatus;
	}

	public EDeviceStatus getRunStatus() {
		return runStatus;
	}

	public void setRunStatus(EDeviceStatus runStatus) {
		this.runStatus = runStatus;
	}

	public EDeviceType getDevType() {
		return devType;
	}

	public void setDevType(EDeviceType devType) {
		this.devType = devType;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getCurExtVector() {
		return curExtVector;
	}

	public void setCurExtVector(double curExtVector) {
		this.curExtVector = curExtVector;
	}

	public double getCurExtSum() {
		return curExtSum;
	}

	public void setCurExtSum(double curExtSum) {
		this.curExtSum = curExtSum;
	}

	public double getCurExtRate() {
		return curExtRate;
	}

	public void setCurExtRate(double curExtRate) {
		this.curExtRate = curExtRate;
	}

	public int getCurExtTime() {
		return curExtTime;
	}

	public void setCurExtTime(int curExtTime) {
		this.curExtTime = curExtTime;
	}
	
	
	
}
