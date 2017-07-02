package com.dfkj.fcp.protocol.hardware.parse.impl;

import com.dfkj.fcp.core.constant.EDeviceStatus;
import com.dfkj.fcp.core.constant.EDeviceType;
import com.dfkj.fcp.core.constant.EValueStatus;
import com.dfkj.fcp.core.util.ByteArray;
import com.dfkj.fcp.core.vo.DataMessageItem;
import com.dfkj.fcp.protocol.hardware.parse.DataItemParseAnnotation;
import com.dfkj.fcp.protocol.hardware.parse.IDataItemParseGtw1P1;

/**
 * 0x21 风速数据
 * @author songfei
 * @date 2016-05-26
 */
@DataItemParseAnnotation(dataTypeId = 0x21)
public class DataItemParse0x21Impl implements IDataItemParseGtw1P1 {

	public DataMessageItem[] parse(ByteArray content,int channelNo, int positionNo) {
		DataMessageItem item = new DataMessageItem();
		
		short windSpeed = content.getShortAt(0);
		content.removeAt(0, 2);
		
		item.setValue(windSpeed / 10.0);
		/**
		 * 风速单位为m/s， 需转为 km/h
		 */
		item.setValue(3.6 * item.getValue());
		
		item.setDevType(EDeviceType.WIND_SPEED);
		item.setChannelNo(channelNo);
		item.setPositionNo(positionNo);
		item.setDevStatus(EDeviceStatus.NORMAL);
		item.setValStatus(EValueStatus.VALID);
		if (windSpeed >= 0x7F00) {
			item.setValStatus(EValueStatus.INVALID);
		}
		
		return new DataMessageItem[] { item };
	}

}
