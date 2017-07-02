package com.dfkj.fcp.protocol.hardware.parse.impl;

import com.dfkj.fcp.core.constant.EDeviceStatus;
import com.dfkj.fcp.core.constant.EDeviceType;
import com.dfkj.fcp.core.constant.EValueStatus;
import com.dfkj.fcp.core.util.ByteArray;
import com.dfkj.fcp.core.vo.DataMessageItem;
import com.dfkj.fcp.protocol.hardware.parse.DataItemParseAnnotation;
import com.dfkj.fcp.protocol.hardware.parse.IDataItemParseGtw1P1;

/**
 * 03 温湿度数据
 * @author Administrator
 *
 */
@DataItemParseAnnotation(dataTypeId = 0x03)
public class DataItemParse0x03Impl implements IDataItemParseGtw1P1 {
	
	public DataMessageItem[] parse(ByteArray content, int channelNo, int positionNo) {
		DataMessageItem[] items = new DataMessageItem[2];
		EValueStatus valStatus = content.size() >= 4 ? EValueStatus.VALID : EValueStatus.INVALID;
		
        short temperature = content.getShortAt(0);//(short)(content.getAt(0) << 8 | content.getAt(1));
        short humidity = content.getShortAt(2);//(short)(content.getAt(2) << 8 | content.getAt(3));

        content.removeAt(0, 4);

        items[0] = new DataMessageItem();
    	items[0].setValue(temperature / 100.0);
        items[0].setDevType(EDeviceType.AIR_TEMPERATURE);
        items[0].setChannelNo(channelNo);
        items[0].setPositionNo(positionNo);
        items[0].setValStatus(valStatus);
        items[0].setDevStatus(EDeviceStatus.NORMAL);
        
        if (temperature == 0x7F01 || temperature == 0x7F02)
        {
            items[0].setValStatus(EValueStatus.INVALID);
            
            if (temperature == 0x7F01)
                items[0].setDevStatus(EDeviceStatus.NO_ACCESS); // 未接
            else if (temperature == 0x7F02)
            	items[0].setDevStatus(EDeviceStatus.TROUBLE);	//	故障
        }

        items[1] = new DataMessageItem();
    	items[1].setValue(humidity / 100.0);
        items[1].setDevType(EDeviceType.AIR_HUMIDITY);
        items[1].setChannelNo(1);
        items[1].setPositionNo(positionNo);
        items[1].setValStatus(valStatus);
        items[1].setDevStatus(EDeviceStatus.NORMAL);
        
        if (temperature == 0x7F01 || temperature == 0x7F02)
        {
            items[1].setValStatus(EValueStatus.INVALID);
            
            if (temperature == 0x7F01)
                items[1].setDevStatus(EDeviceStatus.NO_ACCESS); // 未接
            else if (temperature == 0x7F02)
            	items[1].setDevStatus(EDeviceStatus.TROUBLE);	//	故障
        }
        
		return items;
	}

}
