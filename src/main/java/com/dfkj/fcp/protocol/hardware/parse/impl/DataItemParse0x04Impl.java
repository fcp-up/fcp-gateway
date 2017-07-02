package com.dfkj.fcp.protocol.hardware.parse.impl;

import com.dfkj.fcp.core.constant.EDeviceStatus;
import com.dfkj.fcp.core.constant.EDeviceType;
import com.dfkj.fcp.core.constant.EValueStatus;
import com.dfkj.fcp.core.util.ByteArray;
import com.dfkj.fcp.core.vo.DataMessageItem;
import com.dfkj.fcp.protocol.hardware.parse.DataItemParseAnnotation;
import com.dfkj.fcp.protocol.hardware.parse.IDataItemParseGtw1P1;

/**
 * 0x04 空气温度
 * @author songfei
 * @date 2016-05-26
 */
@DataItemParseAnnotation(dataTypeId = 0x04, dataLength = 2, multiple = 10.0, deviceType = EDeviceType.AIR_TEMPERATURE)
public class DataItemParse0x04Impl implements IDataItemParseGtw1P1 {
	
	public DataMessageItem[] parse(ByteArray content,int channelNo, int positionNo) {
		DataMessageItem item = new DataMessageItem();
		
		short airTemp = content.getShortAt(0);
		content.removeAt(0, 2);
		
		item.setValue(airTemp / 10.0);
		item.setDevType(EDeviceType.AIR_TEMPERATURE);
		item.setChannelNo(channelNo);
		item.setPositionNo(positionNo);
		item.setDevStatus(EDeviceStatus.NORMAL);
		item.setValStatus(EValueStatus.VALID);
		if (airTemp >= 0x7F00) {
			item.setValStatus(EValueStatus.INVALID);
		}
		
		return new DataMessageItem[] { item };
	}

}
