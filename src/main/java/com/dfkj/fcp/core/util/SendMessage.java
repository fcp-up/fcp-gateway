package com.dfkj.fcp.core.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONArray;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendMessage {
	
	//编码格式。发送编码格式统一用UTF-8
    private static String ENCODING = "UTF-8";
    
    //短信发送地址
    private static String SEND_ALARM_URL = "https://127.0.0.1/terminal/requestAlarm";

    //终端上线地址
    private static String SEND_ONLINE_URL = "https://127.0.0.1/terminal/postOnline";
    
    /**
     * 发送短信
     *
     * @return json格式字符串
     * @throws java.io.IOException
     */

     public String sendAlarmMsg(JSONArray smsListObject) throws IOException, URISyntaxException {
         Map<String, Object> params = new HashMap<String, Object>();
         params.put("params", smsListObject);
         return post(SEND_ALARM_URL, params);
     }     
     
     /**
      * 发送短信
      *
      * @return json格式字符串
      * @throws java.io.IOException
      */

      public String sendOnlineMsg() throws IOException, URISyntaxException {
          Map<String, String> params = new HashMap<String, String>();
          params.put("apikey", "222");
          return post(SEND_ALARM_URL, params);
      }  
     
     
    
    /**
     * 基于HttpClient 4.3的通用POST方法
     *
     * @param url  提交的URL
     * @param paramsMap 提交<参数，值>Map
     * @return 提交响应
     */
     public static String post(String url, Map<String, Object> paramsMap) {
         CloseableHttpClient client = HttpClients.createDefault();
         String responseText = "";
         CloseableHttpResponse response = null;
             try {
                 HttpPost method = new HttpPost(url);
                 if (paramsMap != null) {
                     List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                     for (Map.Entry<String, Object> param : paramsMap.entrySet()) {
                         NameValuePair pair = new BasicNameValuePair(param.getKey(), param.getValue());
                         paramList.add(pair);
                     }
                     method.setEntity(new UrlEncodedFormEntity(paramList, ENCODING));
                 }
                 response = client.execute(method);
                 HttpEntity entity = response.getEntity();
                 if (entity != null) {
                     responseText = EntityUtils.toString(entity, ENCODING);
                 }
             } catch (Exception e) {
                 e.printStackTrace();
             } finally {
                 try {
                     response.close();
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
             }
             System.out.println(responseText);
             return responseText;
         }
}
