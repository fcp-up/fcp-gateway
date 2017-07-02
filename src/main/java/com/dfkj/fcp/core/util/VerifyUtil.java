package com.dfkj.fcp.core.util;

/**
 * 判定工具类
 *
 * @Author JiangWenGuang
 */
public class VerifyUtil {

	/**
	 * 校验码指从消息头开始，同后一字节异或，直到校验码前一个字节，占用一个字节
	 *
	 * @param bytes
	 * @return
	 */
	public final static byte calcVerifyValue(ByteArray bytes) {
		byte verifyVal = 0;
		int size = bytes.size();
		for (int idx = 0; idx < size; idx++) {
			verifyVal ^= bytes.getAt(idx);
		}
		return verifyVal;
	}
	/*public static void main(String arg[]){
		ByteArray byte = new ByteArray(HexHelper.HexStringToBytes("", ""));
	}*/
}
