package com.dfkj.fcp.core.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 优先级
 * @author songfei
 *
 */
public enum EPriorityLvl {

	LOW(0), MEDIUM(1), HIGH(2);
	
	private final static Map<Integer, String> descriptionMap = new HashMap<Integer, String>() {
		private static final long serialVersionUID = 1L;

		{
			put(0, "低");
			put(1, "中");
			put(2, "高");
		}
	};
	
	private int level = 0;

	private EPriorityLvl(int lvl) {
		this.level = lvl;
	}
	
	public int getValue() {
		return this.level;
	}
	
	@Override
	public String toString() {
		return descriptionMap.get(level);
	}
	
}
