package com.dfkj.fcp.protocol.hardware.mina;


import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import com.dfkj.fcp.core.constant.EMessageType;
import com.dfkj.fcp.core.logger.AcpLogger;
import com.dfkj.fcp.core.util.AccessUtils;
import com.dfkj.fcp.core.util.HttpRequestUtils;
import com.dfkj.fcp.core.vo.Message;
import net.sf.json.JSONObject;

public class HardwareServerHandler extends IoHandlerAdapter {

    private final static AcpLogger logger = new AcpLogger(HardwareServerHandler.class);

    public void messageReceived(IoSession session, Object message) throws Exception {  
    	
    	if(!(message instanceof Message)){
    		return ;
    	}
    	
    	try{
    		//存储数据
        	String requestURL = AccessUtils.ALARM_SERVICE;
        	String onlineURL = AccessUtils.ONLINE_SERVICE;
        	Message msg = (Message)message;
        	JSONObject reqJsonParam = new JSONObject();
    		JSONObject resJsonParam  = new JSONObject();  
        	if(msg.getMsgType() == EMessageType.HELLO){
        		requestURL = AccessUtils.ONLINE_SERVICE;
        		reqJsonParam.put("devId", msg.getDeviceId());
        		reqJsonParam.put("online", 1);
        		resJsonParam = HttpRequestUtils.httpPost(requestURL,(JSONObject)reqJsonParam);
        	}else if(msg.getMsgType() == EMessageType.SENSOR_DATA){
        		requestURL = AccessUtils.ALARM_SERVICE;
        		reqJsonParam.put("devId", msg.getDeviceId());
        		reqJsonParam.put("isAlarm", msg.getIsAlarm());
            	reqJsonParam.put("pressure", msg.getVoltage());         	 
            	resJsonParam = HttpRequestUtils.httpPost(requestURL,(JSONObject)reqJsonParam);
        	}    		
    	}catch(Exception e){
    		e.printStackTrace();
    		logger.info("业务处理失败.");
    	}finally{
    		//消息缓存处理
    	}
    }
}
