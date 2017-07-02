package com.dfkj.fcp.protocol.platform.vo;

import java.util.List;

/**
 * 控制指定的设备
 * @author songfei
 * @date 2016-06-22
 */
public class ControllerCommandSpecCmd extends ControllerCommandCmd {

	private List<ControllerCommandSpecItem> data;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append(super.toString());
		do {
			if (data == null)
				break;
			
			builder.append("\n");
			
			for (ControllerCommandSpecItem item : data) {
				builder.append(item.toString());
				builder.append("\n");
			}
			
		} while (false);
		
		return builder.toString();
	}
	
	public List<ControllerCommandSpecItem> getData() {
		return data;
	}

	public void setData(List<ControllerCommandSpecItem> data) {
		this.data = data;
	}
	
}
