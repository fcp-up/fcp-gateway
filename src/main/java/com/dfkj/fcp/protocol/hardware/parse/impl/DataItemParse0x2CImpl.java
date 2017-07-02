package com.dfkj.fcp.protocol.hardware.parse.impl;

import com.dfkj.fcp.core.constant.EDeviceStatus;
import com.dfkj.fcp.core.constant.EDeviceType;
import com.dfkj.fcp.core.constant.EValueStatus;
import com.dfkj.fcp.core.util.ByteArray;
import com.dfkj.fcp.core.vo.DataMessageItem;
import com.dfkj.fcp.protocol.hardware.parse.DataItemParseAnnotation;
import com.dfkj.fcp.protocol.hardware.parse.IDataItemParseGtw1P1;

/**
 * 0x2C 土壤温度数据
 * @author songfei
 * @date 2016-05-26
 */
@DataItemParseAnnotation(dataTypeId = 0x2C)
public class DataItemParse0x2CImpl implements IDataItemParseGtw1P1 {

	public DataMessageItem[] parse(ByteArray content, int channelNo, int positionNo) {
		DataMessageItem item = new DataMessageItem();
		
		short soilTemp = content.getShortAt(0);
		content.removeAt(0, 2);
		
		item.setValue(soilTemp / 10.0);
		item.setDevType(EDeviceType.SOIL_TEMPERATURE);
		item.setChannelNo(channelNo);
		item.setPositionNo(positionNo);
		item.setDevStatus(EDeviceStatus.NORMAL);
		item.setValStatus(EValueStatus.VALID);
		if (soilTemp >= 0x7F00 && soilTemp <= 0x7F02) {
			item.setValStatus(EValueStatus.INVALID);
		}
		
		return new DataMessageItem[] { item };
	}

}
