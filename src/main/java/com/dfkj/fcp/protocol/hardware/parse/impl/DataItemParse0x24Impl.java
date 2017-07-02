package com.dfkj.fcp.protocol.hardware.parse.impl;

import com.dfkj.fcp.core.constant.EDeviceStatus;
import com.dfkj.fcp.core.constant.EDeviceType;
import com.dfkj.fcp.core.constant.EValueStatus;
import com.dfkj.fcp.core.util.ByteArray;
import com.dfkj.fcp.core.vo.DataMessageItem;
import com.dfkj.fcp.protocol.hardware.parse.DataItemParseAnnotation;
import com.dfkj.fcp.protocol.hardware.parse.IDataItemParseGtw1P1;

/**
 * 0x24 氧含量数据
 * @author longda
 * @date 2016-10-30
 */
@DataItemParseAnnotation(dataTypeId = 0x24)
public class DataItemParse0x24Impl implements IDataItemParseGtw1P1 {

	public DataMessageItem[] parse(ByteArray content,int channelNo, int positionNo) {
		DataMessageItem item = new DataMessageItem();
		
		short o2Value = content.getShortAt(0);
		content.removeAt(0, 2);
		
		item.setValue(o2Value / 100.0);
		item.setDevType(EDeviceType.O2);
		item.setChannelNo(channelNo);
		item.setPositionNo(positionNo);
		item.setDevStatus(EDeviceStatus.NORMAL);
		item.setValStatus(EValueStatus.VALID);
		if (o2Value >= 0x7F00) {
			item.setValStatus(EValueStatus.INVALID);
		}
		
		return new DataMessageItem[] { item };
	}

}