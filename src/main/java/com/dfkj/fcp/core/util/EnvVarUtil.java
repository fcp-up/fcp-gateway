package com.dfkj.fcp.core.util;

import java.io.IOException;
import java.io.InputStream;

/**
 * 环境变量工具类
 * @author songfei
 *
 */
public class EnvVarUtil {

	private static EnvVarUtil instance = null;

	private OS_TYPE osType = OS_TYPE.UNKNOWN;	//	操作系统
	private IEnvVarOperate envOperate = null;

	private EnvVarUtil() {
		initOsType();
		
		if (osType == OS_TYPE.WINDOWS) {
			envOperate = new WinEnvVarOperate();
		}
		else if (osType == OS_TYPE.LINUX) {
			envOperate = new LinuxEnvVarOperate();
		}
		else {
			envOperate = null;
		}

	}
	
	public static synchronized EnvVarUtil getInstance() {
		if (instance == null) {
			instance = new EnvVarUtil();
		}
		return instance;
	}
	
	public String get(String key) {
		if (envOperate != null) {
			return envOperate.get(key);
		}
		else {
			return null;
		}
	}
	
	public boolean set(String key, String value) {
		if (envOperate != null) {
			return envOperate.set(key, value);
		}
		else {
			return false;
		}
	}
	
	/**
	 * 判断当前操作系统的类型
	 */
	private void initOsType() {
		String osname = System.getProperty("os.name").toLowerCase();
		if (osname.indexOf("window") != -1) {
			osType = OS_TYPE.WINDOWS;
		}
		else if (osname.indexOf("linux") != -1) {
			osType = OS_TYPE.LINUX;
		}
		else {
			osType = OS_TYPE.UNKNOWN;
		}
	}
	
	private enum OS_TYPE {
		UNKNOWN,WINDOWS,LINUX,ANDROID;
	}
	
	protected interface IEnvVarOperate {
		public String get(String key);
		public boolean set(String key, String value);
	}
	
	private class WinEnvVarOperate implements IEnvVarOperate {

		public String get(String key) {
			String command = String.format("set %s", key);
			String result = procCommand(command);
			
			if (result == null) return "";
			
			int splitIndex = result.indexOf("=");
			if (splitIndex != -1) {
				result = result.substring(splitIndex + 1);
			}
			
			return result;
		}

		public boolean set(String key, String value) {
			return false;
			/*
			String command = String.format("set %s=%s", key, value);
			if (procCommand(command) == null) {
				return false;
			}
			else {
				return true;
			}*/
		}
		
		private String procCommand(String command) {
			String result = "";
	    	try {
				Process process = Runtime.getRuntime().exec("cmd /C " + command);
				InputStream input = process.getInputStream();
				StringBuilder builder = new StringBuilder();
				
				int ch = 0;
				do {
					if (ch != -1 && ch != 0) {
						builder.append((char)ch);
					}
					
					ch = input.read();
				} while(ch != -1);
				
				result = builder.toString().replaceAll("\n", "").replaceAll("\r", "");
				
			} catch (IOException e) {
				result = null;
			}
	    	return result;
		}
	}
	
	private class LinuxEnvVarOperate implements IEnvVarOperate {

		public String get(String key) {
			return null;
		}

		public boolean set(String key, String value) {
			return false;
		}
		
	}
}