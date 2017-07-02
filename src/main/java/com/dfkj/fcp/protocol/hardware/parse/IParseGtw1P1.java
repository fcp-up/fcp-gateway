package com.dfkj.fcp.protocol.hardware.parse;

import com.dfkj.fcp.core.util.ByteArray;
import com.dfkj.fcp.core.vo.Message;

public interface IParseGtw1P1 {
	/**
	 * 解析报文数据
	 *
	 * @param message 报文消息
	 * @param content
	 * @return
	 */
	Message parse(Message message, ByteArray content);
}
