package com.dfkj.fcp.protocol.hardware.parse;

import com.dfkj.fcp.core.util.ByteArray;
import com.dfkj.fcp.core.vo.DataMessageItem;

public interface IDataItemParseGtw1P1 {
	/**
	 * 解析单项数据
	 *
	 * 注意在解析时，需把已解析了的数据从content中移除
	 * @param content 待解析的数据
	 * @param channelNo 通道编号
	 * @param positionNo 采集点
	 * @return
	 */
	DataMessageItem[] parse(ByteArray content, int channelNo,int positionNo);
}
