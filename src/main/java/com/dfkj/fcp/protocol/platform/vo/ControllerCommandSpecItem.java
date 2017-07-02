package com.dfkj.fcp.protocol.platform.vo;

/**
 * ControllerCommandSpecCmd中的项
 * @author songfei
 * @date 2016-06-22
 */
public class ControllerCommandSpecItem {

	private int rid;
	private int aid;
	
	@Override
	public String toString() {
		return String.format("通道号:%d 位置:%d", rid, aid);
	}
	
	public int getRid() {
		return rid;
	}
	public void setRid(int rid) {
		this.rid = rid;
	}
	public int getAid() {
		return aid;
	}
	public void setAid(int aid) {
		this.aid = aid;
	}
	
	
}
