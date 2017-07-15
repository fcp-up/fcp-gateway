package com.dfkj.fcp.protocol.hardware.mina;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.bag.SynchronizedBag;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import com.dfkj.fcp.core.constant.EMessageType;
import com.dfkj.fcp.core.logger.AcpLogger;
import com.dfkj.fcp.core.util.AccessUtils;
import com.dfkj.fcp.core.util.ArrayListUtil;
import com.dfkj.fcp.core.util.HttpRequestUtils;
import com.dfkj.fcp.core.vo.Message;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class HardwareServerHandler extends IoHandlerAdapter {

    private final static AcpLogger logger = new AcpLogger(HardwareServerHandler.class);

    public void messageReceived(IoSession session, Object message) throws Exception {
    	/*synchronized(message){*/
    		if(!(message instanceof ArrayList)){
        		return;
        	}    	
        	try{
        		@SuppressWarnings("unchecked")
        		ArrayList<Message> recvMsg = (ArrayList<Message>)message;
        		//ArrayListUtil.removeDuplicate(recvMsg); 		
        		int len = recvMsg.size();
        		int i = 0;
        		while(i < len){
        			Message msg = (Message)recvMsg.get(i);    	
                	JSONObject messageObj = new JSONObject();
                	JSONArray messageList = new JSONArray();
                	String requestURL="";
                	if(msg.getMsgType() == EMessageType.HELLO){
                		requestURL = AccessUtils.ONLINE_SERVICE;
                		messageObj.put("terminalNo", msg.getCenterNo());
                		messageObj.put("state", 1);
                		messageObj.put("terminalSignal", msg.getCenterSignal());
                	}else if(msg.getMsgType() == EMessageType.SENSOR_DATA){
                		requestURL = AccessUtils.ALARM_SERVICE;  
                		messageObj.put("terminalNo", msg.getCenterNo());
                		messageObj.put("deviceNo", msg.getDeviceId());
                		messageObj.put("isAlarm", msg.getIsAlarm());
                		messageObj.put("pressure", msg.getVoltage()); 
                		messageObj.put("deviceSignal", msg.getDeviceSignal());
          		     }  
                	messageList.add(messageObj);  
            		Map<String,String> reqJsonParam = new HashMap<>();
            		reqJsonParam.put("params", messageList.toString());
                	HttpRequestUtils.httpPost(requestURL,reqJsonParam);
                	i += 1;
        		}
        	}catch(Exception e){
        		e.printStackTrace();
        		logger.info("业务处理失败.");
        	}finally{
        		//消息缓存处理    		
        	}
/*    	}  */    	
    	
    }
    
}
