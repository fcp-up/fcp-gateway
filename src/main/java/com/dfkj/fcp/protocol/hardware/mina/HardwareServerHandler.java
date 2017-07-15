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
    		if(!(message instanceof ArrayList)){
        		return;
        	}    	
        	try{
        		@SuppressWarnings("unchecked")
        		ArrayList<Message> recvMsg = (ArrayList<Message>)message;
        		//ArrayListUtil.removeDuplicate(recvMsg); 		
        		int len = recvMsg.size();
        		int i = 0;
        		JSONArray messageOnLineList = new JSONArray();
        		JSONArray messageAlarmList = new JSONArray();
        		String requestOnLineURL = AccessUtils.ONLINE_SERVICE;
        		String requestAlarmLineURL = AccessUtils.ALARM_SERVICE; 
        		while(i < len){
        			Message msg = (Message)recvMsg.get(i);    	
                	JSONObject messageObj = new JSONObject();                	
                	if(msg.getMsgType() == EMessageType.HELLO){
                		messageObj.put("terminalNo", msg.getCenterNo());
                		messageObj.put("state", 1);
                		messageObj.put("terminalSignal", msg.getCenterSignal());
                		messageOnLineList.add(messageObj); 
                	}else if(msg.getMsgType() == EMessageType.SENSOR_DATA){
                		messageObj.put("terminalNo", msg.getCenterNo());
                		messageObj.put("deviceNo", msg.getDeviceId());
                		messageObj.put("isAlarm", msg.getIsAlarm());
                		messageObj.put("pressure", msg.getVoltage()); 
                		messageObj.put("deviceSignal", msg.getDeviceSignal());
                		messageAlarmList.add(messageObj); 
          		     } 
                	i += 1;
        		}
        		if(messageOnLineList.size()>0){
        			Map<String,String> reqJsonOnLineParam = new HashMap<>();
            		reqJsonOnLineParam.put("params", messageOnLineList.toString());
                	HttpRequestUtils.httpPost(requestOnLineURL,reqJsonOnLineParam);  
                	messageOnLineList.clear();
        		}        		
        		if(messageAlarmList.size()>0){
        			Map<String,String> reqJsonAlarmParam = new HashMap<>();
                	reqJsonAlarmParam.put("params", messageAlarmList.toString());
                	HttpRequestUtils.httpPost(requestAlarmLineURL,reqJsonAlarmParam);
                	messageAlarmList.clear();
        		}     		          	
            	recvMsg.clear();            	
        	}catch(Exception e){
        		e.printStackTrace();
        		logger.info("业务处理失败.");
        	}finally{
        		//消息缓存处理    		
        	}    	
    }
    
}
