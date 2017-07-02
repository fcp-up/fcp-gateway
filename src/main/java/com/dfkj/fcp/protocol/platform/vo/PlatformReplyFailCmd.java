package com.dfkj.fcp.protocol.platform.vo;

/**
 * 平台应答（失败）
 * @author songfei
 * @date 2016-06-17
 */
public class PlatformReplyFailCmd extends PlatformReplySuccessCmd {

	private boolean err;
	private String msg;
	private boolean retry;
	
	@Override
	public String toString() {
		return String.format("%s 失败原因:%s 是否重传:%s", super.toString(), msg, retry ? "是" : "否");
	}
	
	public boolean isErr() {
		return err;
	}
	public void setErr(boolean err) {
		this.err = err;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public boolean isRetry() {
		return retry;
	}
	public void setRetry(boolean retry) {
		this.retry = retry;
	}

}