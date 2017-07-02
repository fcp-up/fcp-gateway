package com.dfkj.fcp.core.vo;

import com.dfkj.fcp.core.constant.EDeviceCategory;
import com.dfkj.fcp.core.constant.EMessageType;
import com.dfkj.fcp.core.constant.EPriorityLvl;
import com.dfkj.fcp.core.util.FormatUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息类
 */
public class Message implements Cloneable,Serializable {

	protected int sequence;
	protected long ack; // 应答标志位： ack < 0时，此字段无效即非应答   >0 应答(对应的应答流水号)
	protected int id;	//	id，以保证唯一标识
	protected String deviceNo;
	protected String orgData;
	protected Date recvMsgDate; // 收到message时的时间
	protected EMessageType msgType; // 消息类型
	protected EDeviceCategory devCategory; // 设备类别
	protected EPriorityLvl ptyLvl; // 优先级
	protected boolean hasSubPackage; // 是否有分包
	protected int subPackageIndex; // 分包序号
	protected int version;	//	版本号
	protected Object tag;	//	标记（可用作绑定其他未在Message中定义的数据）
	
	public Message() {
		ack = -1;
		ptyLvl = EPriorityLvl.MEDIUM;
		hasSubPackage = false;
		subPackageIndex = 0;
	}

	public Message(String orgData) {
		this();
		this.orgData = orgData;
	}
	
	public Message(EMessageType messageType) {
		this.msgType = messageType;
	}
	
	public Object clone() {
		Object o = null;
		try {
			o = super.clone();
		}
		catch(CloneNotSupportedException e) {
			o = null;
		}
		return o;
	}

	@Override
	public int hashCode() {
		String value = deviceNo.replaceAll("-", "");
		int hash = Integer.parseInt(value);
        return hash;
	}
	
	@Override
	public boolean equals(Object o) {
		return (this.hashCode() == o.hashCode());
	}
	
	@Override
	public String toString() {
		return String.format("设备号:%s 版本号:%d 流水号:%d 设备类别:%s 消息类型:%s 优先级:%s 接收时间:%s", 
				this.deviceNo, this.version, this.sequence,
				this.devCategory, this.msgType, this.ptyLvl, 
				recvMsgDate == null ? "?" : FormatUtil.DATE_FORMAT.format(recvMsgDate));
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public long getAck() {
		return ack;
	}

	public void setAck(int ack) {
		this.ack = ack;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDeviceNo() {
		return deviceNo;
	}

	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}

	public String getOrgData() {
		return orgData;
	}

	public void setOrgData(String orgData) {
		this.orgData = orgData;
	}

	public Date getRecvMsgDate() {
		return recvMsgDate;
	}

	public void setRecvMsgDate(Date recvMsgDate) {
		this.recvMsgDate = recvMsgDate;
	}

	public EMessageType getMsgType() {
		return msgType;
	}

	public void setMsgType(EMessageType msgType) {
		this.msgType = msgType;
	}

	public EDeviceCategory getDevCategory() {
		return devCategory;
	}

	public void setDevCategory(EDeviceCategory devCategory) {
		this.devCategory = devCategory;
	}

	public EPriorityLvl getPtyLvl() {
		return ptyLvl;
	}

	public void setPtyLvl(EPriorityLvl ptyLvl) {
		this.ptyLvl = ptyLvl;
	}

	public boolean isHasSubPackage() {
		return hasSubPackage;
	}

	public void setHasSubPackage(boolean hasSubPackage) {
		this.hasSubPackage = hasSubPackage;
	}

	public int getSubPackageIndex() {
		return subPackageIndex;
	}

	public void setSubPackageIndex(int subPackageIndex) {
		this.subPackageIndex = subPackageIndex;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Object getTag() {
		return tag;
	}

	public void setTag(Object tag) {
		this.tag = tag;
	}

}
