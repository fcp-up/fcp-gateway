package com.dfkj.fcp.core.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 设备类别
 * @author songfei
 *
 */
public enum EDeviceCategory {

	COMMON(0), GATEWAY(1), CONTROLLER(2), SENSOR(3),DETECTOR(4);
	
	private final static Map<Integer, String> descriptionMap = new HashMap<Integer, String>() {
		private static final long serialVersionUID = 1L;
		{
			put(0, "网关");
			put(1, "传感器");
			put(2, "控制器");
			put(3, "通用设备");
			put(4,"探测器");
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
