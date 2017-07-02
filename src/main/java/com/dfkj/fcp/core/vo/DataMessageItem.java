package com.dfkj.fcp.core.vo;

import com.dfkj.fcp.core.constant.EDeviceStatus;
import com.dfkj.fcp.core.constant.EDeviceType;
import com.dfkj.fcp.core.constant.EValueStatus;
import com.dfkj.fcp.core.util.FormatUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据项
 * 
 * @author songfei
 *
 */
public class DataMessageItem implements Cloneable,Serializable {

	private String deviceId; 		// 设备ID 00 00
	private int channelNo; 			// 通道号  00 00
	private int positionNo; 		// 采集点  00 00
	private EDeviceType devType; 	// 类型 00 00
	private EDeviceStatus devStatus; // 设备状态
	private double value; 	// 数值  00 | 01
	private Date date; 		// 数据时间  00 00 00 00
	private EValueStatus valStatus;	//	数值的状态  00 00

	@Override
	public String toString() {
		return String.format("设备ID:%s 设备类型:%s 通道号:%d 采集点:%d 状态:%s 数值:%s 数值状态:%s 时间:%s", 
				this.getDeviceId(), this.getDevType(), this.getChannelNo(), this.getPositionNo(), this.getDevStatus(),
				FormatUtil.DEC_FORMAT.format(this.getValue()),
				this.getValStatus(),
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

	public int getPositionNo() {
		return positionNo;
	}

	public void setPositionNo(int positionNo) {
		this.positionNo = positionNo;
	}

	public EDeviceType getDevType() {
		return devType;
	}

	public void setDevType(EDeviceType devType) {
		this.devType = devType;
	}

	public EDeviceStatus getDevStatus() {
		return devStatus;
	}

	public void setDevStatus(EDeviceStatus devStatus) {
		this.devStatus = devStatus;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public EValueStatus getValStatus() {
		return valStatus;
	}

	public void setValStatus(EValueStatus valStatus) {
		this.valStatus = valStatus;
	}
}
