package com.dfkj.fcp.core.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 设备类型
 */
public enum EDeviceType {

	UNKNOWN(-1), //GATEWAY(1),

	/* 传感器(从200开始，对应EDeviceCategory) */
	AIR_TEMPERATURE(200),	//空气温度
	AIR_HUMIDITY(201),		//空气湿度
	ELECTRICITY(202),		//	电量
	VOLTAGE(203),			//	电压
	O2(204),				//	氧含量
	WIND_SPEED(205),		//	风速
	RAINFALL(206),		//	雨量
	SOIL_TEMPERATURE(207),	//	土壤温度
	SOIL_HUMIDITY(208),	//	土壤水分
	PH(209),				//	PH值
	ILLUMINATION(210),	//	照度
	WIND_DIRECTION(211),		//	风向
	WATER_TEMPERATURE(212),	//	水温
	SALINITY(213),		//	盐分
	LIQUID_LVL(214),		//	液位
	CO2(215),			//CO2
	WATER_GAGE(216),//液压
	WATER_YIELD(217),//液量
	LIQUID_LEVEL_STATE(218),//液位状态
	CONDUCTIVITY(219),//电导率

	/* 控制器(从300开始，对应EDeviceCategory)*/
	GENERAL_SWITCH(300),	//	普通开关
	STATUS_SWITCH(301),	//	状态开关  
	CTL_SWITCH(302),	//	控制开关
	TRIGGER_SWITCH(303),//	触发开关
	MECHANICAL_ROTATION_SWITCH(304),//	机械旋转触发开关
	TIME(1000),	//	时间	
	DETECTOR(119);//探测器
	
	private final static Map<Integer, String> descriptionMap = new HashMap<Integer, String>() {
		private static final long serialVersionUID = 1L;

		{
			put(-1, "未知");
			put(1, "网关");
			put(200, "空气温度传感器");
			put(201, "空气湿度传感器");
			put(202, "电量传感器");
			put(203, "电压传感器");
			put(204, "氧含量传感器");
			put(205, "风速传感器");
			put(206, "雨量传感器");
			put(207, "土壤温度传感器");
			put(208, "土壤水分传感器");
			put(209, "PH值传感器");
			put(210, "照度传感器");
			put(211, "风向传感器");
			put(212, "水温传感器");
			put(213, "盐分传感器");
			put(214, "液位传感器");
			put(215, "CO2浓度传感器");
			put(216, "液压传感器");
			put(217, "液流传感器");
			put(218, "液位状态传感器");
			put(219, "电导率传感器");
			put(300, "普通开关");
			put(301, "状态开关");
			put(302, "控制开关");
			put(303, "触发开关");
			put(304, "机械旋转开关");
			put(1000, "时间");
			put(119,"探测器");
		}
	};
	
	private int deviceType;

	private EDeviceType(int tp) {
		this.deviceType = tp;
	}
	
	@Override
	public String toString() {
		return descriptionMap.get(deviceType);
	}
}
