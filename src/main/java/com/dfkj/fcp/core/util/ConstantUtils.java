package com.dfkj.fcp.core.util;
/**
 * 自定义常量类
 * 
 * @author Orcanil
 *
 */
public class ConstantUtils {

	/**
	 * 响应体内容类型：application/json;charset=utf-8
	 */
	public static final String RESPONSE_CONTENT_TYPE = "application/json;charset=utf-8";

	/**
	 * 是否标志：1 - 是
	 */
	public static final Integer YES_OR_NO_YES = 1;

	/**
	 * 是否标志：0 - 否
	 */
	public static final Integer YES_OR_NO_NO = 0;

	/**
	 * 返回状态：1 - 成功
	 */
	public static final Integer RESULTWRAP_FLAG_OK = 1;

	/**
	 * 返回状态：0 - 失败
	 */
	public static final Integer RESULTWRAP_FLAG_ERROR = 0;

	/**
	 * 返回状态：2 - 失败-拒绝再次尝试
	 */
	public static final Integer RESULTWRAP_FLAG_ERROR_NOT_TRY = 2;

	/**
	 * 返回状态：3 - 失败-Token校验不通过
	 */
	public static final Integer RESULTWRAP_FLAG_ERROR_TOKEN = 3;

	/**
	 * 返回状态：4 - 失败-没有权限
	 */
	public static final Integer RESULTWRAP_FLAG_ERROR_NO_PERMISSION = 4;

	/**
	 * 用户令牌：Access-Token
	 */
	public static final String ACCESS_TOKEN = "Access-Token";

	/**
	 * 游客机构：999999
	 */
	public static final Integer ORG_TOURIST = 999999;

}
