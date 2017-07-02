package com.dfkj.fcp.core.util;

public class BCDUtil {

	public static byte ConvertToBCD(byte b)
    {
        //高四位
        byte b1 = (byte)(b / 10);
        //低四位
        byte b2 = (byte)(b % 10);


        return (byte)((b1 << 4) | b2);
    }

    /// <summary>
    /// 将BCD一字节数据转换到byte 十进制数据
    /// </summary>
    /// <param name="b" />字节数
    /// <returns>返回转换后的BCD码</returns>
    public static byte ConvertFromBCD(byte b)
    {
        //高四位
        byte b1 = (byte)((b >> 4) & 0xF);
        //低四位
        byte b2 = (byte)(b & 0xF);

        return (byte)(b1 * 10 + b2);
    }
}
