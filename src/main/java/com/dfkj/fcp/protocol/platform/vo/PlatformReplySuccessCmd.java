package com.dfkj.fcp.protocol.platform.vo;

import com.dfkj.fcp.core.util.FormatUtil;

/**
 * 平台应答（成功）
 */
public class PlatformReplySuccessCmd extends Cmd {

	protected int ack;
	protected String srv;
	protected String gtw;
	protected String dev;
	
	@Override
	public String toString() {
		return String.format("协议号:%d 流水号:%d 应答序号:%d 指令编号:%d 服务器编号:%s 网关:%s 设备号:%s 时间:%s", 
				ver, idx, ack, cmd, 
				srv == null ? "" : srv, 
				gtw == null ? "" : gtw, 
				dev == null ? "" : dev, 
				date == null ? "?" : FormatUtil.DATE_FORMAT.format(date)
				);
	}
	
	public int getAck() {
		return ack;
	}
	public void setAck(int ack) {
		this.ack = ack;
	}
	public String getSrv() {
		return srv;
	}
	public void setSrv(String srv) {
		this.srv = srv;
	}

	public String getDev() {
		return dev;
	}

	public void setDev(String dev) {
		this.dev = dev;
	}

	public String getGtw() {
		return gtw;
	}

	public void setGtw(String gtw) {
		this.gtw = gtw;
	}
	
	
}
