package com.dfkj.fcp.protocol.platform.vo;

import java.util.List;

/**
 * 控制器状态汇报cmd
 * @author songfei
 * @date 2016-06-22
 */
public class ControllerStatusCmd extends GatewaySendCmd {

	private Integer ori;	//	类似于ack
	private Integer ack;	//	ack与ori只能使用一个
	private String dev;
	private List<ControllerStatusItem> data;

	public ControllerStatusCmd() {
		super();
		cmd = 5;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append(super.toString());
		builder.append(" 设备号:" + dev);
		do {
			if (data == null)
				break;
			
			builder.append("\n");
			
			for (ControllerStatusItem item : data) {
				builder.append(item.toString());
				builder.append("\n");
			}
			
		} while (false);
		
		return builder.toString();
	}
	
	public Integer getOri() {
		return ori;
	}
	public void setOri(Integer ori) {
		this.ori = ori;
	}
	public List<ControllerStatusItem> getData() {
		return data;
	}
	public void setData(List<ControllerStatusItem> data) {
		this.data = data;
	}

	public String getDev() {
		return dev;
	}

	public void setDev(String dev) {
		this.dev = dev;
	}

	public Integer getAck() {
		return ack;
	}

	public void setAck(Integer ack) {
		this.ack = ack;
	}
	
	
}
