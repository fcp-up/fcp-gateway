package com.dfkj.fcp.protocol.hardware.parse.impl;

import com.dfkj.fcp.core.constant.EDeviceStatus;
import com.dfkj.fcp.core.constant.EDeviceType;
import com.dfkj.fcp.core.constant.EValueStatus;
import com.dfkj.fcp.core.util.ByteArray;
import com.dfkj.fcp.core.vo.DataMessageItem;
import com.dfkj.fcp.protocol.hardware.parse.DataItemParseAnnotation;
import com.dfkj.fcp.protocol.hardware.parse.IDataItemParseGtw1P1;

/**
 * 0x30 液位状态传感器数据
 * @author jiangwenguang
 * @date 2017-05-12
 */
@DataItemParseAnnotation(dataTypeId = 0x30)
public class DataItemParse0x30Impl implements IDataItemParseGtw1P1 {

	public DataMessageItem[] parse(ByteArray content,int channelNo, int positionNo) {
		DataMessageItem item = new DataMessageItem();

		short value = content.getBeginByte();
		content.removeBeginByte();
		
		item.setValue(value);
		item.setDevType(EDeviceType.LIQUID_LEVEL_STATE);
		item.setChannelNo(channelNo);
		item.setPositionNo(positionNo);
		item.setDevStatus(EDeviceStatus.NORMAL);
		item.setValStatus(EValueStatus.VALID);
		if (value >= 0x7F00) {
			item.setValStatus(EValueStatus.INVALID);
		}
		
		return new DataMessageItem[] { item };
	}

}