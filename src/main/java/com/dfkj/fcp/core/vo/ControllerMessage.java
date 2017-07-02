package com.dfkj.fcp.core.vo;

import com.dfkj.fcp.core.constant.EMessageType;
import com.dfkj.fcp.core.constant.EPriorityLvl;
import com.dfkj.fcp.core.util.PlusList;

/**
 * 控制器Message（即状态）
 * 
 * @author songfei
 * @date 2016-05-31
 */
public class ControllerMessage extends Message {

	private PlusList<ControllerMessageItem> items;

	public ControllerMessage() {
		super(EMessageType.CTRL_REPORT_STATUS);
	}
	
	public static ControllerMessage create(Message message) {
		ControllerMessage ctrlMessage = new ControllerMessage();

		ctrlMessage.setSequence(message.getSequence());
		ctrlMessage.setAck((int)message.getAck());
		ctrlMessage.setId(message.getId());
		ctrlMessage.setDeviceNo(message.getDeviceNo());
		ctrlMessage.setOrgData(message.getOrgData());
		ctrlMessage.setRecvMsgDate(message.getRecvMsgDate());
		ctrlMessage.setMsgType(message.getMsgType());
		ctrlMessage.setDevCategory(message.getDevCategory());
		ctrlMessage.setPtyLvl(EPriorityLvl.HIGH);  // 默认为高优先级
		ctrlMessage.setVersion(message.getVersion());
		
		ctrlMessage.items = null;

		return ctrlMessage;
	}

	@Override
	public Object clone() {
		ControllerMessage o = (ControllerMessage)super.clone();
		if (o == null) {
			return null;
		}
		o.items = new PlusList<ControllerMessageItem>();
		for (ControllerMessageItem item : this.items) {
			o.items.add((ControllerMessageItem)item.clone());
		}
		return o;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append(super.toString());
		do {
			if (items == null)
				break;
			
			builder.append("\n");
			
			for (ControllerMessageItem item : items) {
				builder.append(item.toString());
				builder.append("\n");
			}
			
		} while (false);
		
		return builder.toString();
	}

	public PlusList<ControllerMessageItem> getItems() {
		return items;
	}

	public void setItems(PlusList<ControllerMessageItem> items) {
		this.items = items;
	}

}
