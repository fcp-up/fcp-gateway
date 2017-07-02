package com.dfkj.fcp.core.constant;

import java.util.HashMap;
import java.util.Map;

public enum EAction {

	NO_ACTION(-1), OPEN(0), CLOSE(1), STOP(2);
	
	private final static Map<Integer, String> descriptionMap = new HashMap<Integer, String>() {
		private static final long serialVersionUID = 1L;

		{
			put(-1, "没有动作");
			put(0, "打开");
			put(1, "关闭");
			put(2, "停止");
		}
	};
	
	private int action;

	private EAction(int act) {
		this.action = act;
	}
	
	@Override
	public String toString() {
		return descriptionMap.get(action);
	}
}
