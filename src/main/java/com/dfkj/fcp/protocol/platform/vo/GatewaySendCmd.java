package com.dfkj.fcp.protocol.platform.vo;

import com.dfkj.fcp.core.util.FormatUtil;

/**
 * 网关主发的CMD
 * 具体的字段参考【协议201605251726.js】文档，同时字段名和定义中一致（为了JSON的序列化）
 * @author songfei
 * @date 2016-06-17
 */
public class GatewaySendCmd extends Cmd {

	protected String gtw;
	
	@Override
	public String toString() {
		return String.format("协议号:%d 流水号:%d 指令编号:%d 网关编号:%s 时间:%s", 
				ver, idx, cmd, gtw, date == null ? "?" : FormatUtil.DATE_FORMAT.format(date)
				);
	}

	public String getGtw() {
		return gtw;
	}

	public void setGtw(String gtw) {
		this.gtw = gtw;
	}

}
