package com.dfkj.fcp.protocol.hardware.mina;


import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import com.dfkj.fcp.core.constant.EMessageType;
import com.dfkj.fcp.core.logger.AcpLogger;
import com.dfkj.fcp.core.util.AccessUtils;
import com.dfkj.fcp.core.util.HttpRequestUtils;
import com.dfkj.fcp.core.vo.Message;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class HardwareServerHandler extends IoHandlerAdapter {

    private final static AcpLogger logger = new AcpLogger(HardwareServerHandler.class);

    public void messageReceived(IoSession session, Object message) throws Exception {   
    	
    	if(!(message instanceof Message)){
    		return;
    	}    	
    	try{
        	Message msg = (Message)message;        	
        	JSONObject messageObj = new JSONObject();
        	JSONArray messageList = new JSONArray();
        	String requestURL="";
        	if(msg.getMsgType() == EMessageType.HELLO){
        		requestURL = AccessUtils.ONLINE_SERVICE;
        		messageObj.put("terminalNo", msg.getDeviceId());
        		messageObj.put("state", 1);
        	}else if(msg.getMsgType() == EMessageType.SENSOR_DATA){
        		requestURL = AccessUtils.ALARM_SERVICE;        		
        		messageObj.put("deviceNo", msg.getDeviceId());
        		messageObj.put("isAlarm", msg.getIsAlarm());
        		messageObj.put("pressure", msg.getVoltage()); 
  		     }  
        	messageList.add(messageObj);  
    		Map<String,String> reqJsonParam = new HashMap<>();
    		reqJsonParam.put("params", messageList.toString());
        	HttpRequestUtils.httpPost(requestURL,reqJsonParam);
    	}catch(Exception e){
    		e.printStackTrace();
    		logger.info("业务处理失败.");
    	}finally{
    		//消息缓存处理    		
    	}
    }
}
