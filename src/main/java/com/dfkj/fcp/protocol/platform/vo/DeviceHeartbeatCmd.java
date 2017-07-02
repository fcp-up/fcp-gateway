package com.dfkj.fcp.protocol.platform.vo;


import com.dfkj.fcp.core.util.FormatUtil;

/**
 * 设备心跳Cmd
 * @author songfei
 * @date 2016-06-21
 */
public class DeviceHeartbeatCmd extends GatewaySendCmd {

	protected String dev;	//	设备编号

	public DeviceHeartbeatCmd() {
		super();
		this.cmd = 1;	//	cmd为1
	}
	
	@Override
	public String toString() {
		return String.format("协议号:%d 流水号:%d 指令编号:%d 网关编号:%s 设备编号:%s 时间:%s", 
				ver, idx, cmd, gtw, dev, date == null ? "?" : FormatUtil.DATE_FORMAT.format(date)
				);
	}
	
	public String getDev() {
		return dev;
	}

	public void setDev(String dev) {
		this.dev = dev;
	}
	
	
}
