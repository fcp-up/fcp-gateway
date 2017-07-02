package com.dfkj.fcp.protocol.hardware.parse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dfkj.fcp.core.util.PackageUtil;

/**
 * 管理IDataItemParseGtw1P1接口的实现与对应数据类似Id的映射关系
 * @author songfei
 * @date 2016-05-26
 */
public class DataItemParseManage {

	private final static String PACK_NAME = "com.gstar.acp.protocol.hardware";
	private final static String INTERFACE_NAME = "com.gstar.acp.protocol.hardware.parse.IDataItemParseGtw1P1";
	private static DataItemParseManage manage = null;
	private Map<Integer, Class<?>> maps = null;
	
	private DataItemParseManage() {
		
	}
	
	public static synchronized DataItemParseManage createInstance() {
		if (manage == null) {
			manage = new DataItemParseManage();
			if (!init()) {
				manage = null;
			}
		}
		return manage;
	}
	
	public Class<?> getParse(int dataTypeId) {
		if (manage == null) {
			return null;
		}
		else {
			if (manage.maps.containsKey(dataTypeId)) {
				return manage.maps.get(dataTypeId);
			}
			else {
				return null;
			}
		}
	}
	
	private static boolean init() {
		manage.maps = new HashMap<Integer, Class<?>>();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		List<String> cls = PackageUtil.getClassName(loader, PACK_NAME);
		
		//	遍历所有类，查看是否实现接口IDataItemParseGtw1P1
		for (String c : cls) {
			if (isImplDataItemParseGtw1P1(c)) {
				register(c);
			}
		}
		return true;
	}
	
	private static boolean isImplDataItemParseGtw1P1(String cls) {
		try {
			Class<?>[] ifaces = Class.forName(cls).getInterfaces();
			for (Class<?> ifc : ifaces) {
				if (ifc.getName() == INTERFACE_NAME) {
					return true;
				}
			}
		} catch (ClassNotFoundException e) {
		}
		return false;
	}
	
	private static void register(String cls) {
		try {
			DataItemParseAnnotation annotation = Class.forName(cls).getAnnotation(DataItemParseAnnotation.class);
			if (annotation != null) {
				manage.maps.put(annotation.dataTypeId(), Class.forName(cls));
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
