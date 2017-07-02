package com.dfkj.fcp.core.constant;

import java.util.HashMap;
import java.util.Map;

public enum EFrameType {
	IS_HELLO(1),
	IS_HEARTBEAT(2),
	IS_DATAPACK(3);
	private final static Map<Integer, String> descriptionMap = new HashMap<Integer, String>() {
		private static final long serialVersionUID = 1L;

		{
			put(1, "握手包");
			put(2, "心跳包");
			put(3, "数据报");
		}
	};
	
	private int framType;

	private EFrameType(int tp) {
		this.framType = tp;
	}
	
	@Override
	public String toString() {
		return descriptionMap.get(framType);
	}
}
