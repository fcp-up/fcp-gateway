package com.dfkj.fcp.core.vo;

import java.io.Serializable;
import java.util.Date;

import com.dfkj.fcp.core.util.FormatUtil;

public class HelloMessage implements Cloneable, Serializable {
	
	protected String centerNo; //集中器ID
	protected int terminalSignal;//集中器信号强度
	protected int state;//在线标记
	protected Date stateDate;//当前时间
	
	public HelloMessage(String centerNo,int terminalSignal,int state,Date stateDate){
		this.centerNo = centerNo;
		this.terminalSignal = terminalSignal;
		this.state = state;
		this.stateDate = stateDate;
	}
	
	public String getCenterNo() {
		return centerNo;
	}
	
	public void setCenterNo(String centerNo) {
		this.centerNo = centerNo;
	}
	
	public int getTerminalSignal() {
		return terminalSignal;
	}

	public void setTerminalSignal(int terminalSignal) {
		this.terminalSignal = terminalSignal;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Date getStateDate() {
		return stateDate;
	}

	public void setStateDate(Date stateDate) {
		this.stateDate = stateDate;
	}

	@Override
	public int hashCode() {
		String value = centerNo;
		int hash = Integer.parseInt(value);
        return hash;
	}
	
	@Override
	public boolean equals(Object o) {
		return (this.hashCode() == o.hashCode());
	}	
	
	@Override
	public String toString() {
		return String.format("集中器编号:%s 集中器信号强度:%d 状态标记:%d 状态时间:%s", 
				this.centerNo,this.terminalSignal,this.state,
				this.stateDate == null ? "?" : FormatUtil.DATE_FORMAT.format(stateDate),
				this.state,this.stateDate);
	}

}
