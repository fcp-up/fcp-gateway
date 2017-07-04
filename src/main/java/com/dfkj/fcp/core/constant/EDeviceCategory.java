package com.dfkj.fcp.core.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 设备类别
 * @author songfei
 *
 */
public enum EDeviceCategory {

	UNKNOWN(0), GATEWAY(1), SENSOR(2);
	
	private final static Map<Integer, String> descriptionMap = new HashMap<Integer, String>() {
		private static final long serialVersionUID = 1L;
		{
			put(0, "未知");
			put(1, "网关");
			put(2, "传感器");
		}
	};
	
	private int category;

	private EDeviceCategory(int ctg) {
		this.category = ctg;
	}
	
	@Override
	public String toString() {
		return descriptionMap.get(category);
	}
}
