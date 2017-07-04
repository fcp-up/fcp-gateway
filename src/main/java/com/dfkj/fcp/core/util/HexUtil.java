package com.dfkj.fcp.core.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public final class HexUtil {

	public static String ByteToString(byte[] sourceByteArray, String gap) {
		if (sourceByteArray == null)
			return null;

		StringBuilder builder = new StringBuilder();

		for (int idx = 0; idx < sourceByteArray.length; idx++) {
			builder.append(String.format("%02X", sourceByteArray[idx]));
			if (gap != null && gap.length() > 0 && idx != sourceByteArray.length - 1) {
				builder.append(gap);
			}
		}
		return builder.toString();
	}

	public static String ByteToString(byte[] sourceByteArray) {
		return ByteToString(sourceByteArray, null);
	}

	public static String ShortToString(short sValue, String gap) {
		return ByteToString(new byte[] { (byte) (sValue >> 8 & 0xFF), (byte) (sValue >> 0 & 0xFF) }, gap);
	}

	public static String IntToString(int iValue, String gap) {
		return ByteToString(new byte[] { (byte) (iValue >> 24 & 0xFF), (byte) (iValue >> 16 & 0xFF),
				(byte) (iValue >> 8 & 0xFF), (byte) (iValue >> 0 & 0xFF) }, gap);
	}

	public static String LongToString(long lValue, String gap) {
		return ByteToString(new byte[] { (byte) (lValue >> 56 & 0xFF), (byte) (lValue >> 48 & 0xFF),
				(byte) (lValue >> 40 & 0xFF), (byte) (lValue >> 32 & 0xFF), (byte) (lValue >> 24 & 0xFF),
				(byte) (lValue >> 16 & 0xFF), (byte) (lValue >> 8 & 0xFF), (byte) (lValue >> 0 & 0xFF) }, gap);
	}

	public static byte[] HexStringToBytes(String str, String gap) {
		if (str == null)
			return null;

		str = str.replaceAll(gap, "");

		String digital = "0123456789ABCDEF";
		char[] hex2char = str.toCharArray();
		byte[] bytes = new byte[str.length() / 2];
		int temp;
		for (int i = 0; i < bytes.length; i++) {
			temp = digital.indexOf(hex2char[2 * i]) * 16;
			temp += digital.indexOf(hex2char[2 * i + 1]);
			bytes[i] = (byte) (temp & 0xff);
		}

		return bytes;
	}
	
	public static char[] getChars (byte[] bytes) {
	      Charset cs = Charset.forName ("UTF-8");
	      ByteBuffer bb = ByteBuffer.allocate (bytes.length);
	      bb.put (bytes);
	      bb.flip ();
	       CharBuffer cb = cs.decode (bb);	  
	   return cb.array();
	}
	
	/*public static byte[] HexStringToBytes(String str, String gap) {
		if (str == null)
			return null;

		str = str.replaceAll(gap, "");

		int length = str.length() / 2;
		byte[] bytes = new byte[length];

		for (int idx = 0; idx < str.length();) {
			System.out.println("str:" + str.substring(idx, idx + 2));
			// bytes[idx /2] = Byte.parseByte(str.substring(idx, idx + 2));
			bytes[idx / 2] = Byte.parseByte(str.substring(idx, idx + 2), 16);
			idx += 2;
		}
		return bytes;
	}*/
	/*public static void main(String args[]){
		byte[] a = null;
		a =HexStringToBytes("04-91","-");
		System.out.println(ByteToString(a,"-"));
	}*/
}
