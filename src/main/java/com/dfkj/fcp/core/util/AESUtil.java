package com.dfkj.fcp.core.util;

import org.springframework.util.Base64Utils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * AES加密工具类
 *
 * @Author JiangWenGuang
 */
public class AESUtil {
	/**
	 * 
	 */
	private static String PASSWORD = "f9476bedaa9fef0daf077f5fd33f8738";
	/**
	 * 编码
	 */
	private static String ENCODE = "UTF-8";

	/**
	 * 加密
	 * 
	 * @param content
	 *            需要加密的内容
	 * @return
	 */
	public static String encrypt(String content) {
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
			
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(PASSWORD.getBytes(ENCODE));
			keyGenerator.init(128, secureRandom);
			
			SecretKey secretKey = keyGenerator.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器   
			byte[] byteContent = content.getBytes(ENCODE);
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化   
			byte[] result = cipher.doFinal(byteContent);
			return Base64Utils.encodeToUrlSafeString(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密
	 * 
	 * @param content
	 *            待解密内容
	 * @return
	 */
	public static String decrypt(String content) {
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");

			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(PASSWORD.getBytes(ENCODE));
			keyGenerator.init(128, secureRandom);
			
			SecretKey secretKey = keyGenerator.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器   
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			byte[] src = Base64Utils.decodeFromUrlSafeString(content);
			byte[] result = cipher.doFinal(src);
			return new String(result, ENCODE);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
