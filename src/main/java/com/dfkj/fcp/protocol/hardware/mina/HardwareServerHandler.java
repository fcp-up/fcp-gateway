package com.dfkj.fcp.protocol.hardware.mina;


import java.util.ArrayList;
import java.util.Date;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import com.dfkj.fcp.core.constant.EMessageType;
import com.dfkj.fcp.core.logger.AcpLogger;
import com.dfkj.fcp.core.util.AccessUtils;
import com.dfkj.fcp.core.vo.HelloMessage;
import com.dfkj.fcp.core.vo.Message;
import com.dfkj.fcp.protocol.hardware.factory.HardwareInstanceFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class HardwareServerHandler extends IoHandlerAdapter {

    private final static AcpLogger logger = new AcpLogger(HardwareServerHandler.class);
	JSONObject messageObj = new JSONObject();     
	JSONArray messageOnLineList = new JSONArray();
	JSONArray messageAlarmList = new JSONArray();
	String requestOnLineURL = AccessUtils.ONLINE_SERVICE;
	String requestAlarmLineURL = AccessUtils.ALARM_SERVICE; 

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
        		messageOnLineList.clear();
        		messageAlarmList.clear();
        		while(i < len){
        			Message msg = (Message)recvMsg.get(i);   	
        			if(msg.getMsgType() == EMessageType.HELLO){
                		messageObj.put("terminalNo", msg.getCenterNo());
                		messageObj.put("state", 1);
                		messageObj.put("terminalSignal", msg.getCenterSignal());
//                		messageObj.put("stateTime", msg.getRecvMsgDate());
                		messageOnLineList.add(messageObj);    
                		HardwareInstanceFactory.getMinaServerInstance().refreshState(msg,session);
                		logger.info("集中器【" + msg.getCenterNo() + "】上线.");
                	}else if(msg.getMsgType() == EMessageType.SENSOR_DATA){
                		messageObj.put("terminalNo", msg.getCenterNo());
                		messageObj.put("deviceNo", msg.getDeviceId());
                		messageObj.put("isAlarm", msg.getIsAlarm());
                		messageObj.put("pressure", msg.getVoltage()); 
                		messageObj.put("deviceSignal", msg.getDeviceSignal());
                		messageAlarmList.add(messageObj); 
                		HardwareInstanceFactory.getMinaServerInstance().refreshState(msg,session);
                		logger.info("集中器【" + msg.getCenterNo() + "-" + msg.getDeviceId()  + "】请求报警.");
          		     } 
                	i += 1;
        		}
        		if(messageOnLineList.size()>0){
        			HardwareInstanceFactory.getMinaServerInstance().reportData(messageOnLineList,requestOnLineURL);
        			messageOnLineList.clear();
        		}        		
        		if(messageAlarmList.size()>0){
        			HardwareInstanceFactory.getMinaServerInstance().reportData(messageAlarmList,requestAlarmLineURL); 
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
    
    public void sessionOpened(IoSession session) throws Exception {
        // Empty handler
    	HardwareInstanceFactory.getMinaServerInstance().terminalsMap.put(session.getId(), null);
    }    
    
    public void sessionClosed(IoSession session) throws Exception {
        // Empty handler
    	HelloMessage msg =  HardwareInstanceFactory.getMinaServerInstance().terminalsMap.get(session.getId());
    	if(null != msg){
    		//设置当前终端在线状态
    		messageOnLineList.clear();
    		msg.setState(0);
    		msg.setStateDate(new Date());
    		//向平台汇报当前状态
    		logger.info("集中器【" + msg.getCenterNo() + "】离线.");
    		messageObj.put("terminalNo", msg.getCenterNo());
    		messageObj.put("state", msg.getState());
    		messageOnLineList.add(messageObj);
    		HardwareInstanceFactory.getMinaServerInstance().reportData(messageOnLineList,requestOnLineURL);
    	}
    	logger.info("session " + Long.toString(session.getId()) + " 断开.");
    }
    
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
    	HelloMessage msg =  HardwareInstanceFactory.getMinaServerInstance().terminalsMap.get(session.getId());
    	if(null != msg){
        	messageOnLineList.clear();
    		//设置当前终端在线状态
    		msg.setState(0);
    		msg.setStateDate(new Date());
    		logger.info(msg.toString());
    		//向平台汇报当前状态
    		logger.info("集中器【" + msg.getCenterNo() + "】离线.");
    		messageObj.put("terminalNo", msg.getCenterNo());
    		messageObj.put("state", msg.getState());    		
    		messageOnLineList.add(messageObj);
    		HardwareInstanceFactory.getMinaServerInstance().reportData(messageOnLineList,requestOnLineURL);
    	}
    	HardwareInstanceFactory.getMinaServerInstance().terminalsMap.remove(session.getId());
    }    
//    //汇报数据
//    public void reportData(JSONArray dataList,String url){
//    	Map<String,String> reqJsonParam = new HashMap<>();
//    	reqJsonParam.put("params", dataList.toString());
//        HttpRequestUtils.httpPost(url,reqJsonParam);
//        dataList.clear();
//    }
//    
//    //刷新终端当前状态
//    public void refreshState(Message msg,IoSession session) {
//    	HelloMessage helloMsg = new HelloMessage(msg.getCenterNo(),msg.getCenterSignal(),1,msg.getRecvMsgDate());
//		HardwareInstanceFactory.getMinaServerInstance().terminalsMap.put(session.getId(),helloMsg);
//    }
}