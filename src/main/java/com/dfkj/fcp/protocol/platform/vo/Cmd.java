package com.dfkj.fcp.protocol.platform.vo;

import java.util.Date;

import com.dfkj.fcp.core.util.FormatUtil;

/**
 * Cmd
 * @author songfei
 * @date 2016-06-17
 */
public class Cmd implements Cloneable {

	protected int ver;
	protected int idx;
	protected int cmd;
	protected Date date;
	
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
	public String toString() {
		return String.format("协议号:%d 流水号:%d 指令编号:%d 时间:%s", 
				ver, idx, cmd, date == null ? "?" : FormatUtil.DATE_FORMAT.format(date)
				);
	}
	
	public int getVer() {
		return ver;
	}
	public void setVer(int ver) {
		this.ver = ver;
	}
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public int getCmd() {
		return cmd;
	}
	public void setCmd(int cmd) {
		this.cmd = cmd;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
