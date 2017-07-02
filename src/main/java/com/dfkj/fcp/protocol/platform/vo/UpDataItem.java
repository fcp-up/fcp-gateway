package com.dfkj.fcp.protocol.platform.vo;

import java.util.Date;

import com.dfkj.fcp.core.util.FormatUtil;

/**
 * 设备上传数据指令中具体的数据项
 * @author songfei
 * @date 2016-06-17
 */
public class UpDataItem {

	private int rid;	//	通道号
	private int aid;	//	采集点
	private int devt;	//	设备类型
	private int sta;	//	状态
	private double val;	//	数值
	private Date date;	//	数据时间
	
	@Override
	public String toString() {
		return String.format("通道号:%d 采集点:%d 设备类型:%d 状态:%d 数值:%s 数据时间:%s",
				rid, aid, devt, sta,
				FormatUtil.DEC_FORMAT.format(val),
				date == null ? "?" : FormatUtil.DATE_FORMAT.format(date)
				);
	}
	
	public int getRid() {
		return rid;
	}
	public void setRid(int rid) {
		this.rid = rid;
	}
	public int getAid() {
		return aid;
	}
	public void setAid(int aid) {
		this.aid = aid;
	}
	public int getDevt() {
		return devt;
	}
	public void setDevt(int devt) {
		this.devt = devt;
	}
	public int getSta() {
		return sta;
	}
	public void setSta(int sta) {
		this.sta = sta;
	}
	public double getVal() {
		return val;
	}
	public void setVal(double val) {
		this.val = val;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
