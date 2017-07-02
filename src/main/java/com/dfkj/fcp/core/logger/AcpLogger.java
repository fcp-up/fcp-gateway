package com.dfkj.fcp.core.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AcpLogger {
	private Logger logger = null;
	private Class<?> clazz = null;

	public AcpLogger(Class<?> cls) {
		logger = LoggerFactory.getLogger(cls);
		clazz = cls;
	}

	public void trace(String msg) {
		if (logger == null)
			return;
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		/**
		 * stes[0]:java.lang.Thread.getStackTrace
		 * stes[1]:AcpLogger.trace
		 * stes[2]:为具体调用
		 */
		StackTraceElement ste = stackTrace.length > 2 ? stackTrace[2] : null;
		
		/**
		 * message格式 className[行号] - message
		 */
		logger.trace(String.format("%s[%d] - %s", clazz.getName(), 
				ste == null ? 0 : ste.getLineNumber(), msg));
	}

	/*
	 * public void trace(String msg, Object...arg1) { if (logger == null)
	 * return; logger.trace(msg, arg1); }
	 */
	public void debug(String msg) {
		if (logger == null)
			return;
		logger.debug(msg);
	}

	/*
	 * public void debug(String msg, Object...arg1) { if (logger == null)
	 * return; logger.debug(msg, arg1); }
	 */
	public void info(String msg) {
		if (logger == null)
			return;
		logger.info(msg);
	}

	/*
	 * public void info(String msg, Object...arg1) { if (logger == null) return;
	 * logger.info(msg, arg1); }
	 */
	public void warn(String msg) {
		if (logger == null)
			return;
		logger.warn(msg);
	}

	/*
	 * public void warn(String msg, Object...arg1) { if (logger == null) return;
	 * logger.warn(msg, arg1); }
	 */
	public void error(String msg) {
		logger.error(msg);
	}
	/*
	 * public void error(String msg, Object...arg1) { if (logger == null)
	 * return; logger.error(msg, arg1); }
	 */
}
