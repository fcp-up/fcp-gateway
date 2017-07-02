package com.dfkj.fcp.protocol.hardware.parse.impl;

import com.dfkj.fcp.core.constant.EDeviceStatus;
import com.dfkj.fcp.core.constant.EDeviceType;
import com.dfkj.fcp.core.constant.EValueStatus;
import com.dfkj.fcp.core.util.ByteArray;
import com.dfkj.fcp.core.vo.DataMessageItem;
import com.dfkj.fcp.protocol.hardware.parse.DataItemParseAnnotation;
import com.dfkj.fcp.protocol.hardware.parse.IDataItemParseGtw1P1;

import java.math.BigDecimal;

/**
 * 0x27 液体流量传感器数据
 * @author mhz
 * @date 2017-01-09
 */
@DataItemParseAnnotation(dataTypeId = 0x27)
public class DataItemParse0x27Impl implements IDataItemParseGtw1P1 {

	public DataMessageItem[] parse(ByteArray content, int channelNo, int positionNo) {
		DataMessageItem item = new DataMessageItem();
		
		short dischargeOfWaterValue1 = content.getShortAt(0);
		content.removeAt(0, 4);
		float dischargeOfWaterValue2 = content.byteToFloat2(0);
		content.removeAt(0, 4);
		BigDecimal b = new BigDecimal(String.valueOf(dischargeOfWaterValue1+dischargeOfWaterValue2));  
		Double dischargeOfWaterValue = b.doubleValue();
				
		item.setValue(dischargeOfWaterValue);
		item.setDevType(EDeviceType.WATER_YIELD);
		item.setChannelNo(channelNo);
		item.setPositionNo(positionNo);
		item.setDevStatus(EDeviceStatus.NORMAL);
		item.setValStatus(EValueStatus.VALID);
		if (dischargeOfWaterValue >= 0x7F00) {
			item.setValStatus(EValueStatus.INVALID); 
		}
		
		return new DataMessageItem[] { item };
	}

}