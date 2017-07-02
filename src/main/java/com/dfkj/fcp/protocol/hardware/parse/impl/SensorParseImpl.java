package com.dfkj.fcp.protocol.hardware.parse.impl;

import com.dfkj.fcp.core.constant.EMessageType;
import com.dfkj.fcp.core.logger.AcpLogger;
import com.dfkj.fcp.core.util.ByteArray;
import com.dfkj.fcp.core.util.PlusList;
import com.dfkj.fcp.core.vo.DataMessage;
import com.dfkj.fcp.core.vo.DataMessageItem;
import com.dfkj.fcp.core.vo.Message;
import com.dfkj.fcp.protocol.hardware.parse.DataItemParseManage;
import com.dfkj.fcp.protocol.hardware.parse.IDataItemParseGtw1P1;
import com.dfkj.fcp.protocol.hardware.parse.IParseGtw1P1;

/**
 * 传感器消息体解析
 * @author Administrator
 *
 */
public class SensorParseImpl implements IParseGtw1P1 {

	private final static AcpLogger logger = new AcpLogger(SensorParseImpl.class);
	private static DataItemParseManage parseManage = DataItemParseManage.createInstance();
	
	public Message parse(Message message, ByteArray content) {
		if (message.getMsgType() != EMessageType.SENSOR_DATA) {
			logger.debug("传感器设备，暂只支持数据消息.");
			return message;
		}
		
		DataMessage dataMessage = DataMessage.create(message);
		
		//	数据源地址
        short addrOfSourceData = (short)(content.getAt(0) << 8 | content.getAt(1));
        int channelNo = (addrOfSourceData & 0x00F0) >>> 4; channelNo = channelNo == 0 ?1:channelNo;
        boolean isCombineData = ((addrOfSourceData & 0xF000) == 0x9000);
        int addrOf3 = addrOfSourceData & 0x000F;  //  地址3
        int dataItemCount = isCombineData ? addrOf3 : 1;
        boolean isFirstData = true;
        
        content.removeAt(0, 2);	//	移除数据源地址
        PlusList<DataMessageItem> dataItems = new PlusList<DataMessageItem>();
		
        while (content.size() > 0)
        {
            //  合并的数据只影响第一个数据类型，其后的数据类型对应的数据都为单条数据
            dataItemCount = (isFirstData && isCombineData) ? addrOf3 : 1;

            int dataType = content.getBeginByte();
            content.removeBeginByte();
            
            int positionId = 0;

            for (int dataItem = 0; dataItem < dataItemCount; dataItem++)
            {
                positionId = (isCombineData) ? dataItem + 1 : addrOf3;
                
                Class<?> cls = parseManage.getParse(dataType);
                if (cls == null) {
                	logger.debug(String.format("不支持的数据类型[%2X].", dataType));
                	continue;
                }
                IDataItemParseGtw1P1 parse = null;
				try {
					parse = (IDataItemParseGtw1P1)cls.newInstance();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				
				/*
				 * 暂时没用
				if (positionId == 0) {
					positionId = 1;
				}
				*/
                DataMessageItem[] datas = (parse == null) ? null : parse.parse(content, channelNo, positionId);

                if (datas == null) {
                    continue;
                }

                for (DataMessageItem item : datas)
                {
                	item.setDeviceId(message.getDeviceNo());
                	//2016-10-18 mhz 过滤无效数据
/*                	if(item.getValStatus() == EValueStatus.INVALID){
                		continue;
                	}else{
                		
                		double val = 32513.00;
                		
                		if(item.getValue() == val ){
                			continue ;
                		}
                	}*/
                	dataItems.add(item);
                }
            }
            
            isFirstData = false;
        }
        
        dataMessage.setData(dataItems);

        logger.debug("SensorParseImpl:\n" + dataMessage);
        
		return dataMessage;
	}

	public ByteArray unparse(Message message) {
		return null;
	}

}
