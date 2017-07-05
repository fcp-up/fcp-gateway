package com.dfkj.fcp.core.util;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

public class HttpClientUtils {

	private static final String CHARSET_UTF_8 = "utf-8";

	private static final String CONTENT_TYPE = "application/json";

	/**
	 * 发送get请求
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static ResultWrap doGet(String url) throws IOException {
		return sendGet(url);
	}

	/**
	 * 发送get请求
	 * 
	 * @param url
	 * @return String
	 * @throws IOException
	 */
	public static String doGetReturnString(String url) throws IOException {
		String resStr = null;
		HttpGet get = new HttpGet(url);
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		try {
			response = client.execute(get);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				return "";
			}
			HttpEntity httpEntity = response.getEntity();
			resStr = EntityUtils.toString(httpEntity, CHARSET_UTF_8);// 取出应答字符串
			EntityUtils.consume(httpEntity);
		} catch (ClientProtocolException e) {
			return "";
		} catch (IOException e) {
			return "";
		} finally {
			if (response != null) {
				response.close();
			}
		}
		return resStr;
	}

	/**
	 * 发送post请求
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static ResultWrap doPost(String url, String jsonParam) throws IOException {
		return sendPost(url, jsonParam);
	}

	/**
	 * 发送put请求
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static ResultWrap doPut(String url, String jsonParam) throws IOException {
		return sendPut(url, jsonParam);
	}

	/**
	 * 发送delete请求
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static ResultWrap doDelete(String url) throws IOException {
		return sendDelete(url);
	}

	/**
	 * get请求
	 * 
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private static ResultWrap sendGet(String url) throws ClientProtocolException, IOException {
		ResultWrap rw = new ResultWrap();
		String resStr = null;
		HttpGet get = new HttpGet(url);
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		try {
			response = client.execute(get);
			if (response == null) {
				rw.setFlag(ConstantUtils.RESULTWRAP_FLAG_ERROR);
				rw.setMessage("请求失败，请确认地址正确！");
				return rw;
			}
			rw.setStatusCode(response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				rw.setFlag(ConstantUtils.RESULTWRAP_FLAG_ERROR);
				rw.setMessage(response.getStatusLine().toString());
				return rw;
			}
			HttpEntity httpEntity = response.getEntity();
			resStr = EntityUtils.toString(httpEntity, CHARSET_UTF_8);// 取出应答字符串
			resStr.replaceAll("\r", "");// 去掉返回结果中的"\r"字符，否则会在结果字符串后面显示一个小方格
			rw = JSONObject.parseObject(resStr, ResultWrap.class);
			rw.setStatusCode(response.getStatusLine().getStatusCode());
			EntityUtils.consume(httpEntity);
		} finally {
			if (response != null) {
				response.close();
			}
		}

		return rw;
	}

	/**
	 * Post请求
	 * 
	 * @throws ClientProtocolException
	 * 
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	private static ResultWrap sendPost(String url, String jsonParam) throws ClientProtocolException, IOException {
		ResultWrap rw = new ResultWrap();
		CloseableHttpClient client = HttpClients.createDefault();
		String resStr = null;
		HttpPost post = new HttpPost(url);
		if (!StringUtils.isEmpty(jsonParam)) {
			HttpEntity entity = new StringEntity(jsonParam, ContentType.create(CONTENT_TYPE, CHARSET_UTF_8));
			post.setEntity(entity);
		}

		CloseableHttpResponse response = null;
		try {
			response = client.execute(post);
			if (response == null) {
				rw.setFlag(ConstantUtils.RESULTWRAP_FLAG_ERROR);
				rw.setMessage("请求失败，请确认地址正确！");
				return rw;
			}
			rw.setStatusCode(response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				rw.setFlag(ConstantUtils.RESULTWRAP_FLAG_ERROR);
				rw.setMessage(response.getStatusLine().toString());
				return rw;
			}
			HttpEntity httpEntity = response.getEntity();
			resStr = EntityUtils.toString(httpEntity, CHARSET_UTF_8);// 取出应答字符串
			resStr.replaceAll("\r", "");// 去掉返回结果中的"\r"字符，否则会在结果字符串后面显示一个小方格
			rw = JSONObject.parseObject(resStr, ResultWrap.class);
			rw.setStatusCode(response.getStatusLine().getStatusCode());
			EntityUtils.consume(httpEntity);
		} finally {
			if (response != null) {
				response.close();
			}
		}

		return rw;
	}

	/**
	 * Put请求
	 * 
	 * @throws ClientProtocolException
	 * 
	 * @throws IOException
	 */
	private static ResultWrap sendPut(String url, String jsonParam) throws ClientProtocolException, IOException {
		ResultWrap rw = new ResultWrap();

		String resStr = null;
		HttpPut put = new HttpPut(url);
		if (!StringUtils.isEmpty(jsonParam)) {
			HttpEntity entity = new StringEntity(jsonParam, ContentType.create(CONTENT_TYPE, CHARSET_UTF_8));
			put.setEntity(entity);
		}
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		try {

			response = client.execute(put);
			if (response == null) {
				rw.setFlag(ConstantUtils.RESULTWRAP_FLAG_ERROR);
				rw.setMessage("请求失败，请确认地址正确！");
				return rw;
			}
			rw.setStatusCode(response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				rw.setFlag(ConstantUtils.RESULTWRAP_FLAG_ERROR);
				rw.setMessage(response.getStatusLine().toString());
				return rw;
			}
			HttpEntity httpEntity = response.getEntity();
			resStr = EntityUtils.toString(httpEntity, CHARSET_UTF_8);// 取出应答字符串
			resStr.replaceAll("\r", "");// 去掉返回结果中的"\r"字符，否则会在结果字符串后面显示一个小方格
			rw = JSONObject.parseObject(resStr, ResultWrap.class);
			rw.setStatusCode(response.getStatusLine().getStatusCode());
			EntityUtils.consume(httpEntity);
		} finally {
			if (response != null) {
				response.close();
			}
		}

		return rw;
	}

	/**
	 * Delete请求
	 * 
	 * @throws ClientProtocolException
	 * 
	 * @throws IOException
	 */
	private static ResultWrap sendDelete(String url) throws ClientProtocolException, IOException {
		ResultWrap rw = new ResultWrap();
		String resStr = null;
		HttpDelete del = new HttpDelete(url);
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		try {
			response = client.execute(del);
			if (response == null) {
				rw.setFlag(ConstantUtils.RESULTWRAP_FLAG_ERROR);
				rw.setMessage("请求失败，请确认地址正确！");
				return rw;
			}
			rw.setStatusCode(response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				rw.setFlag(ConstantUtils.RESULTWRAP_FLAG_ERROR);
				rw.setMessage(response.getStatusLine().toString());
				return rw;
			}
			HttpEntity httpEntity = response.getEntity();
			resStr = EntityUtils.toString(httpEntity, CHARSET_UTF_8);// 取出应答字符串
			resStr.replaceAll("\r", "");// 去掉返回结果中的"\r"字符，否则会在结果字符串后面显示一个小方格
			rw = JSONObject.parseObject(resStr, ResultWrap.class);
			rw.setStatusCode(response.getStatusLine().getStatusCode());
			EntityUtils.consume(httpEntity);
		} finally {
			if (response != null) {
				response.close();
			}
		}

		return rw;
	}
}
