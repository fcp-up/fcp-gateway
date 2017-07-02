package com.dfkj.fcp.core.util;

/**
 * 消息流水号
 *
 * @author songfei
 * @date 2016-06-21
 */
public class SequenceUtil {
	private final static int MAX_VALUE = 255;
	private static int curSequence = 0;
	
	public static int generate() {
		curSequence = curSequence > MAX_VALUE ? 0 : curSequence;
		return curSequence;
	}
}
