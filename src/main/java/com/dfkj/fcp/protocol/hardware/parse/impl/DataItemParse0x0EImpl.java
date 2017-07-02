package com.dfkj.fcp.protocol.hardware.parse.impl;

import com.dfkj.fcp.core.constant.EDeviceType;
import com.dfkj.fcp.core.constant.EValueStatus;
import com.dfkj.fcp.core.util.BCDUtil;
import com.dfkj.fcp.core.util.ByteArray;
import com.dfkj.fcp.core.util.FormatUtil;
import com.dfkj.fcp.core.vo.DataMessageItem;
import com.dfkj.fcp.protocol.hardware.parse.DataItemParseAnnotation;
import com.dfkj.fcp.protocol.hardware.parse.IDataItemParseGtw1P1;

/**
 * 0x0E 时间数据
 * @author songfei
 * @date 2016-05-26
 */
@DataItemParseAnnotation(dataTypeId = 0x0E)
public class DataItemParse0x0EImpl implements IDataItemParseGtw1P1 {

	public DataMessageItem[] parse(ByteArray content,int channelNo, int positionNo) {
        short year = BCDUtil.ConvertFromBCD(content.getAt(0));
        short month = BCDUtil.ConvertFromBCD(content.getAt(1));
        short day = BCDUtil.ConvertFromBCD(content.getAt(2));

        short hour = BCDUtil.ConvertFromBCD(content.getAt(3));
        short minute = BCDUtil.ConvertFromBCD(content.getAt(4));
        short second = BCDUtil.ConvertFromBCD(content.getAt(5));
        
        content.removeAt(0, 6);
        
        DataMessageItem item = new DataMessageItem();
        item.setDevType(EDeviceType.TIME);
        item.setValStatus(EValueStatus.VALID);
        
        try {
        item.setDate(FormatUtil.DATE_FORMAT.parse(String.format("20%d-%d-%d %d:%d:%d.000",
        		year, month, day, hour, minute, second)));
        }
        catch (Exception e)  
        {  
        }  
        return new DataMessageItem[] { item };
	}

}
