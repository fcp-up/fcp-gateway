package com.dfkj.fcp.core.util;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class RequestUtil {

	private static Object parseRequestJson(String json) {
		return JSON.parse(json);
	}	

	/**
	 * 
	 * @param requestParam
	 *            前台获取的对象要更新的字段信息
	 * @param retrieveObj
	 *            根据ID从数据库取到的对象
	 * @return 返回对象的最终要更新至的状态
	 */
	public static Map<String, Object> getUpdateEntityByRequestParam(Map<String, Object> requestParam,
			Object retrieveObj) {
		Map<String, Object> newObjMap = new HashMap<String, Object>();
		if (retrieveObj != null && requestParam != null) {
			Class objCls = retrieveObj.getClass();
			Method[] methods = objCls.getMethods();
			if (methods != null && methods.length > 0) {
				for (Method method : methods) {
					String methodName = method.getName();
					if (methodName.startsWith("get") && (methodName.indexOf("Class") == -1)) {
						Integer length = method.getParameterTypes().length;
						if (length == 0) {
							String propertyName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
							Object requestVal = requestParam.get(propertyName);
							try {
								newObjMap.put(propertyName,
										requestVal != null ? requestVal : method.invoke(retrieveObj));
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
			Object newObj = JSON.parseObject(JSON.toJSONStringWithDateFormat(newObjMap, DateTimeUtil.DEFAULT_DATETIME_FORMAT,
					SerializerFeature.WriteDateUseDateFormat), objCls);
			return JSON.parseObject(JSON.toJSONStringWithDateFormat(newObj, DateTimeUtil.DEFAULT_DATETIME_FORMAT,
					SerializerFeature.WriteDateUseDateFormat), Map.class);
		}
		return newObjMap;
	}

	public static Map<String, Object> getValidSqlParamMap(Map<String, Object> requestParam, Class entityClass) {
		Object validObj = JSON.parseObject(JSON.toJSONStringWithDateFormat(requestParam,  DateTimeUtil.DEFAULT_DATETIME_FORMAT,
				SerializerFeature.WriteDateUseDateFormat), entityClass);
		return JSON.parseObject(JSON.toJSONStringWithDateFormat(validObj,  DateTimeUtil.DEFAULT_DATETIME_FORMAT,
				SerializerFeature.WriteDateUseDateFormat), Map.class);
	}

}
