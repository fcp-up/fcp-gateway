package com.dfkj.fcp.protocol.platform.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 简单实现MessageRepository
 * 用于保存当前网关下设备的状态
 * @author songfei
 * @date 2016-06-22
 */
public class SampleMessageRepositoryUtil {

	private static SampleMessageRepositoryUtil repository = null;
	private Map<Integer, Object> dataMap = null;

	private SampleMessageRepositoryUtil() {
		dataMap = new HashMap<Integer, Object>();
	}
	
	public static synchronized SampleMessageRepositoryUtil getInstance() {
		if (repository == null) {
			repository = new SampleMessageRepositoryUtil();
		}
		
		return repository;
	}
	
	public Object getById(int id) {
		if (dataMap.containsKey((Integer) id)) {
			return dataMap.get((Integer) id);
		}
		return null;
	}

	public int save(int id, Object message) {
		dataMap.put(id, message);
		return 0;
	}

}
