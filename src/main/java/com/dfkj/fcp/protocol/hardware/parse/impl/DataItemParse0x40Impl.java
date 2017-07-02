package com.dfkj.fcp.protocol.hardware.parse.impl;


import com.dfkj.fcp.core.constant.EDeviceStatus;
import com.dfkj.fcp.core.constant.EDeviceType;
import com.dfkj.fcp.core.constant.EValueStatus;
import com.dfkj.fcp.core.util.ByteArray;
import com.dfkj.fcp.core.vo.DataMessageItem;
import com.dfkj.fcp.protocol.hardware.parse.DataItemParseAnnotation;
import com.dfkj.fcp.protocol.hardware.parse.IDataItemParseGtw1P1;

/**
 * 0x30 控制器数据
 * @author jiangwenguang
 * @date 2017-05-15
 */
@DataItemParseAnnotation(dataTypeId = 0x40)
public class DataItemParse0x40Impl implements IDataItemParseGtw1P1 {

	public DataMessageItem[] parse(ByteArray content, int channelNo, int positionNo) {
		DataMessageItem item = new DataMessageItem();

		int cno = content.getBeginByte();	//	通道号
		content.removeBeginByte();

		int runStatus = content.getBeginByte();	//	运行状态
		content.removeBeginByte();

		item.setValue(runStatus);
		item.setDevType(EDeviceType.MECHANICAL_ROTATION_SWITCH);
		item.setChannelNo(cno);
		item.setPositionNo(positionNo);
		item.setDevStatus(EDeviceStatus.NORMAL);
		item.setValStatus(EValueStatus.VALID);

		return new DataMessageItem[] { item };
	}

}