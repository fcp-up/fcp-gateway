package com.dfkj.fcp.core.vo;


import com.dfkj.fcp.core.constant.EMessageType;
import com.dfkj.fcp.core.util.PlusList;

/**
 * 数据汇报Message
 * 
 * @author songfei
 *
 */
public class DataMessage extends Message{
	private PlusList<DataMessageItem> data;
	
	public DataMessage() {
		super(EMessageType.SENSOR_DATA);
	}
	
	public static DataMessage create(Message message) {
		DataMessage dataMessage = new DataMessage();
		
		dataMessage.sequence = message.getSequence();
		dataMessage.ack = message.getAck();
		dataMessage.id = message.getId();
		dataMessage.deviceNo = message.getDeviceNo();
		dataMessage.orgData = message.getOrgData();
		dataMessage.recvMsgDate = message.getRecvMsgDate();
		dataMessage.msgType = message.getMsgType();
		dataMessage.devCategory = message.getDevCategory();
		dataMessage.ptyLvl = message.getPtyLvl();
		dataMessage.version = message.getVersion();
		
		dataMessage.data = null;
		
		return dataMessage;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append(super.toString());
		do {
			if (data == null)
				break;
			
			builder.append("\n");
			
			for (DataMessageItem item : data) {
				builder.append(item.toString());
				builder.append("\n");
			}
			
		} while (false);
		
		return builder.toString();
	}
	
	@Override
	public Object clone() {
		DataMessage o = (DataMessage)super.clone();
		if (o == null) {
			return null;
		}
		o.data = new PlusList<DataMessageItem>();
		for (DataMessageItem item : this.data) {
			o.data.add((DataMessageItem)item.clone());
		}
		return o;
	}
	
	public PlusList<DataMessageItem> getData() {
		return data;
	}

	public void setData(PlusList<DataMessageItem> items) {
		if (this.data == null) {
			this.data = new PlusList<DataMessageItem>();
		}
		else {
			this.data.clear();
		}
		this.data.addAll(items);
	}

}
