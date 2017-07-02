package com.dfkj.fcp.protocol.platform.util;

import com.dfkj.fcp.core.constant.*;
import com.dfkj.fcp.core.logger.AcpLogger;
import com.dfkj.fcp.core.vo.ControllerCommandMessage;
import com.dfkj.fcp.core.vo.Message;
import com.dfkj.fcp.core.vo.UniversalResponseMessage;
import com.dfkj.fcp.protocol.platform.vo.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用于cmd到message的转换
 * @author songfei
 * @date 2016-06-22
 */
public class CmdTransformMessageUtil {

	private final static AcpLogger logger = new AcpLogger(CmdTransformMessageUtil.class);
	
	public static Message convertFromCmd(Cmd cmd) {
		return null;
	}
	
	/**
	 * 平台的回复信息
	 * 
	 * @param cmd
	 * @return
	 */
	public static UniversalResponseMessage convertFromPlatformCmd(Cmd cmd) {
		PlatformReplySuccessCmd success = (PlatformReplySuccessCmd)cmd;
		
		UniversalResponseMessage message = new UniversalResponseMessage();
		message.setSequence(cmd.getIdx());
		message.setAck(1);
		
		if (cmd.getCmd() == 0) {
			/**
			 * cmd = 0 表示网关心跳的回复
			 */
			message.setDeviceNo("GSDev01");
		}
		else {
			message.setDeviceNo(success.getDev());
		}
		
		message.setMsgType(EMessageType.UNIVERSAL_RESPONSE);
		
		message.setRecvMsgDate(success.getDate());
		message.setRespDate(success.getDate());
		message.setRespMsgSequence(success.getAck());
		
		/**
		 * 回应回复的消息类型
		 */
		EMessageType msgType = ConstantUtil.convertEMessageTypeByValue(success.getCmd());
		message.setMsgType(msgType);
		if(msgType == EMessageType.CTRL_ACTION_RESP) {
			message.setRespMsgType(EMessageType.CTRL_ACTION);
		}
		
		/**
		 * 设置返回状态
		 */
		PlatformReplyFailCmd failed = (PlatformReplyFailCmd)cmd;
		if (failed.isErr()) {
			message.setRespCode(EUniversalResponseCode.FAILED);
			message.setMsg(failed.getMsg());
		}
		else {
			message.setRespCode(EUniversalResponseCode.OK);
		}
		return message;
	}

	public static List<ControllerCommandMessage> convertFromControllerCommandSpecCmd(ControllerCommandSpecCmd cmd) {
		List<ControllerCommandMessage> actions = new ArrayList<ControllerCommandMessage>();
		
		for (ControllerCommandSpecItem actCmdItem : cmd.getData()) {
			ControllerCommandMessage actionMessage = new ControllerCommandMessage();
		
			actionMessage.setSequence(cmd.getIdx());
			actionMessage.setAck(-1);
			actionMessage.setDeviceNo(cmd.getDev());
			actionMessage.setRecvMsgDate(new Date());
			actionMessage.setDevCategory(EDeviceCategory.CONTROLLER);
			actionMessage.setPtyLvl(EPriorityLvl.HIGH);
			
			actionMessage.setDeviceId(cmd.getDev());
			actionMessage.setChannelNo(actCmdItem.getRid());

			actionMessage.setAction(ConstantUtil.convertEActionByValue(cmd.getAct()));
			actionMessage.setDevType(ConstantUtil.convertEDeiceTypeByValue(cmd.getDevt()));
			
			actionMessage.setId(actionMessage.hashCode());
			
			/**
			 * 设置参数
			 * 不支持 vector，sum，rate设置
			 */
			//actionMessage.setParamVector(cmd.getVec());	//	控制的向量
			//actionMessage.setParamSum(cmd.getSum());	//	控制的总量
			//actionMessage.setParamRate(cmd.getRate());	//	控制的速率
			
			if (actionMessage.getDevType() == EDeviceType.STATUS_SWITCH) {
				//actionMessage.setParamTime(Integer.MAX_VALUE);
			}
			else if (actionMessage.getDevType() == EDeviceType.TRIGGER_SWITCH) {
				if (cmd.getTime() == null) {
					actionMessage.setParamTime(Integer.MAX_VALUE);
				}
				else {
					actionMessage.setParamTime((int)(cmd.getTime().doubleValue()));	//	控制的时间(单位秒)
				}
			}
			else {
				logger.info("现在只支持触发开关.");
				continue;
			}
			
			actions.add(actionMessage);
		}
		
		return actions;
	}
}
