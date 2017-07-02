package com.dfkj.fcp.protocol.platform.vo;

import java.util.Date;

import com.dfkj.fcp.core.util.FormatUtil;

/**
 * 控制器中具体的状态项
 * @author songfei
 * @date 2016-06-22
 */
public class ControllerStatusItem {

	private int rid;
	private int aid;
	private int devt;
	private int sta;
	private double val;
	private Double vec;
	private Double sum;
	private Double rate;
	private Double time;
	private Date date;
	
	@Override
	public String toString() {
		return String.format("通道号:%d 采集点:%d 设备类型:%d 状态:%d 值:%s 当前向量:%s 当前总量:%s 当前速率:%s 当前时间:%s 状态时间:%s",
				rid, aid, devt, sta,
				FormatUtil.DEC_FORMAT.format(val),
				vec == null ? "" : FormatUtil.DEC_FORMAT.format(vec.doubleValue()),
				sum == null ? "" : FormatUtil.DEC_FORMAT.format(sum.doubleValue()),
				rate == null ? "" : FormatUtil.DEC_FORMAT.format(rate.doubleValue()),
				time == null ? "" : FormatUtil.DEC_FORMAT.format(time.doubleValue()),
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
	public void setVal(Double val) {
		this.val = val;
	}
	public Double getVec() {
		return vec;
	}
	public void setVec(Double vec) {
		this.vec = vec;
	}
	public Double getSum() {
		return sum;
	}
	public void setSum(Double sum) {
		this.sum = sum;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public Double getTime() {
		return time;
	}
	public void setTime(Double time) {
		this.time = time;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
