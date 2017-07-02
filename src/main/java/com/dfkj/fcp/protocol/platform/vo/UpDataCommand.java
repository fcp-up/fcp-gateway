package com.dfkj.fcp.protocol.platform.vo;

import java.util.List;

/**
 * 设备上传数据指令
 * @author songfei
 * @date 2016-06-17
 */
public class UpDataCommand extends GatewaySendCmd {

	private List<UpDataItem> data;
	private String dev;
	
	public UpDataCommand() {
		this.cmd = 3;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append(super.toString());
		do {
			if (data == null)
				break;
			
			builder.append("\n");
			
			for (UpDataItem item : data) {
				builder.append(item.toString());
				builder.append("\n");
			}
			
		} while (false);
		
		return builder.toString();
	}
	
	public List<UpDataItem> getData() {
		return data;
	}

	public void setData(List<UpDataItem> data) {
		this.data = data;
	}

	public String getDev() {
		return dev;
	}

	public void setDev(String dev) {
		this.dev = dev;
	}
	
}
