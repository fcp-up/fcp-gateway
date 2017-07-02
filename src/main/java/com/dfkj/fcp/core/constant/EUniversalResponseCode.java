package com.dfkj.fcp.core.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 响应码
 */
public enum EUniversalResponseCode {

	UNKNOWN(-1), OK(0), FAILED(1), MISTAKE(2), NOT_SUPPORT(3), OK_AND_SET(4);
	
	private final static Map<Integer, String> descriptionMap = new HashMap<Integer, String>() {
		private static final long serialVersionUID = 1L;

		{
			put(-1, "未知");
			put(0, "成功/确认");
			put(1, "失败");
			put(2, "消息有误");
			put(3, "不支持");
			put(4, "成功/确认+参数设置");
		}
	};
	
	private int code;
	private EUniversalResponseCode(int c) {
		this.code = c;
	}
	
	@Override
	public String toString() {
		return descriptionMap.get(code);
	}
	
	public int getCode() {
		return this.code;
	}
}
