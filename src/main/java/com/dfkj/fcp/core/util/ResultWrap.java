package com.dfkj.fcp.core.util;

//返回值处理包装
public class ResultWrap {
	
	
	private Integer flag;
	private String message;
	private Object data;
	private Integer statusCode;

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public boolean success() {
		return 0 == getFlag() - 1;
	}

	/**
	 * 操作失败
	 */
	public void error(String message) {
		this.setFlag(0);
		this.setMessage(message);
	}

	public ResultWrap() {
		this.setFlag(1);
	}

}
