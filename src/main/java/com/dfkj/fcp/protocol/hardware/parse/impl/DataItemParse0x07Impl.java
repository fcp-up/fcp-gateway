package com.dfkj.fcp.protocol.hardware.parse.impl;

import com.dfkj.fcp.core.constant.EDeviceStatus;
import com.dfkj.fcp.core.constant.EDeviceType;
import com.dfkj.fcp.core.constant.EValueStatus;
import com.dfkj.fcp.core.util.ByteArray;
import com.dfkj.fcp.core.vo.DataMessageItem;
import com.dfkj.fcp.protocol.hardware.parse.DataItemParseAnnotation;
import com.dfkj.fcp.protocol.hardware.parse.IDataItemParseGtw1P1;

/**
 * 07 电量数据
 * @author Administrator
 *
 */
@DataItemParseAnnotation(dataTypeId = 0x07)
public class DataItemParse0x07Impl implements IDataItemParseGtw1P1 {

	public DataMessageItem[] parse(ByteArray content,int channelNo, int positionNo) {
		DataMessageItem item = new DataMessageItem();
		
		short electricity = content.getBeginByte();
		content.removeBeginByte();
		
		item.setValue(electricity);
		item.setDevType(EDeviceType.ELECTRICITY);
		item.setChannelNo(channelNo);
		item.setPositionNo(positionNo);
		
		item.setValStatus(EValueStatus.VALID);
		if (electricity > 100 || electricity < 0) {
			item.setValStatus(EValueStatus.INVALID);
		}
        item.setDevStatus(EDeviceStatus.NORMAL);
		
        return new DataMessageItem[] { item };
	}

}
