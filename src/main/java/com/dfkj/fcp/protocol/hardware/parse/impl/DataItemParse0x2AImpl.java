package com.dfkj.fcp.protocol.hardware.parse.impl;

import com.dfkj.fcp.core.constant.EDeviceStatus;
import com.dfkj.fcp.core.constant.EDeviceType;
import com.dfkj.fcp.core.constant.EValueStatus;
import com.dfkj.fcp.core.util.ByteArray;
import com.dfkj.fcp.core.vo.DataMessageItem;
import com.dfkj.fcp.protocol.hardware.parse.DataItemParseAnnotation;
import com.dfkj.fcp.protocol.hardware.parse.IDataItemParseGtw1P1;

/**
 * 0x2A PH数据
 * @author songfei
 * @date 2016-05-26
 */
@DataItemParseAnnotation(dataTypeId = 0x2A)
public class DataItemParse0x2AImpl implements IDataItemParseGtw1P1 {

	public DataMessageItem[] parse(ByteArray content,int channelNo, int positionNo) {
		DataMessageItem item = new DataMessageItem();
		
		short ph = content.getShortAt(0);
		content.removeAt(0, 2);
		
		item.setValue(ph / 100.0);
		item.setDevType(EDeviceType.PH);
		item.setChannelNo(channelNo);
		item.setPositionNo(positionNo);
		item.setDevStatus(EDeviceStatus.NORMAL);
		item.setValStatus(EValueStatus.VALID);
		
		if (ph >= 0x7F00) {
			item.setValStatus(EValueStatus.INVALID);
		}
		else if (item.getValue() > 14.0) {
			item.setValStatus(EValueStatus.OVER_MAX);
		}
		else if (item.getValue() < 0.0) {
			item.setValStatus(EValueStatus.LESS_MIN);
		}
		
		return new DataMessageItem[] { item };
	}

}
