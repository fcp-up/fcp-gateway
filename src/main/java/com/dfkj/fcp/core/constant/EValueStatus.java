package com.dfkj.fcp.core.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据的状态
 * @author songfei
 *
 */
public enum EValueStatus {

	VALID(0), INVALID(1), OVER_MAX(2), LESS_MIN(3), INVALID_TIME(4);
	
	private final static Map<Integer, String> descriptionMap = new HashMap<Integer, String>() {
		private static final long serialVersionUID = 1L;

		{
			put(0, "有效");
			put(1, "无效");
			put(2, "大于有效范围");
			put(3, "小于有效范围");
			put(4, "无效的时间");
		}
	};
	
	private int status;
	private EValueStatus(int s) {
		this.status = s;
	}
	
	@Override
	public String toString() {
		return descriptionMap.get(status);
	}
}
