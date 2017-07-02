package com.dfkj.fcp.protocol.hardware.parse.impl;

import com.dfkj.fcp.core.constant.EDeviceStatus;
import com.dfkj.fcp.core.constant.EDeviceType;
import com.dfkj.fcp.core.constant.EValueStatus;
import com.dfkj.fcp.core.util.ByteArray;
import com.dfkj.fcp.core.vo.DataMessageItem;
import com.dfkj.fcp.protocol.hardware.parse.DataItemParseAnnotation;
import com.dfkj.fcp.protocol.hardware.parse.IDataItemParseGtw1P1;

/**
 * 0x30 电导率传感器数据
 * @author jiangwenguang
 * @date 2017-05-12
 */
@DataItemParseAnnotation(dataTypeId = 0x31)
public class DataItemParse0x31Impl implements IDataItemParseGtw1P1 {

	public DataMessageItem[] parse(ByteArray content,int channelNo, int positionNo) {
		DataMessageItem item = new DataMessageItem();

		short conductivity = content.getShortAt(0);
		content.removeAt(0, 2);

		item.setValue(conductivity / 100.0);
		item.setDevType(EDeviceType.CONDUCTIVITY);
		item.setChannelNo(channelNo);
		item.setPositionNo(positionNo);
		item.setDevStatus(EDeviceStatus.NORMAL);
		item.setValStatus(EValueStatus.VALID);
		if (conductivity >= 0x7F00) {
			item.setValStatus(EValueStatus.INVALID);
		}

		return new DataMessageItem[] { item };
	}

}