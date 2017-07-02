package com.dfkj.fcp.protocol.platform.util;

import com.dfkj.fcp.config.SystemConfig;
import com.dfkj.fcp.core.constant.EMessageType;
import com.dfkj.fcp.core.constant.EUniversalResponseCode;
import com.dfkj.fcp.core.constant.EValueStatus;
import com.dfkj.fcp.core.util.SequenceUtil;
import com.dfkj.fcp.core.vo.*;
import com.dfkj.fcp.protocol.platform.vo.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Cmd <----> Message 之间的转换
 * @author songfei
 * @date 2016-06-17
 */
public class MsgTransformCmdUtil {

	/**
	 * 将数据汇报报文转换设备上传数据指令
	 *
	 * @param dataMessage  数据汇报报文
	 * @return
	 */
	public static UpDataCommand convertUpDataCmdByMsg(DataMessage dataMessage) {
		if (dataMessage == null) return null;
		
		UpDataCommand cmd = new UpDataCommand();
		
		cmd.setVer(0);
		cmd.setIdx(dataMessage.getSequence());
		cmd.setGtw(PlatformConnectStatus.getGatewayId());
		//cmd.setDev(dataMessage.getDeviceNo().replace("-", ""));
		cmd.setDev(dataMessage.getDeviceNo());
		cmd.setDate(dataMessage.getRecvMsgDate());
		
		List<UpDataItem> datas = new ArrayList<UpDataItem>();

		int i = 1;
		for (DataMessageItem item : dataMessage.getData()) {
			UpDataItem dataItem = new UpDataItem();
			dataItem.setAid(i++);
			dataItem.setRid(item.getChannelNo());
			dataItem.setDevt(ConstantUtil.convertValueByEDeviceType(item.getDevType()));	//	设备类型
			dataItem.setSta(ConstantUtil.convertValueByEDeviceStatus(item.getDevStatus()));		//	设备状态
			dataItem.setDate(item.getDate());
			dataItem.setVal(item.getValue());
			
        	if(item.getValStatus() == EValueStatus.INVALID){
        		continue;
        	}else{
        		
        		double val = 32513.00;
        		
        		if(item.getValue() == val ){
        			continue ;
        		}
        	}
			
			/**
			 * 有效的数据才上传
			 * 暂时不做处理
			 */
			//if (item.getValStatus() == EValueStatus.VALID) {
			//	datas.add(dataItem);
			//}
			
			/**
			 * 不支持的设备类型
			 */
			if (dataItem.getDevt() == 0) {
				continue;
			}
			
			datas.add(dataItem);
		}

		if(SystemConfig.getProperty(SystemConfig.DEVICE_UPDATA_PARSE_RULE_KEY).equals("1")){
			for(UpDataItem item : datas){
				item.setAid(1);
			}
		}

		if (datas.size() == 0) {
			return null;
		}
		else {
			cmd.setData(datas);
			return cmd;
		}
	}

	/**
	 * 将数据报文转换网关心跳指令
	 *
	 * @param message  数据报文
	 * @return
	 */
	public static GatewaySendCmd convertGatewayHeartCmdByMsg(Message message) {
		if (message == null) return null;
		
		GatewaySendCmd cmd = new GatewaySendCmd();
		cmd.setCmd(0);
		cmd.setVer(0);
		cmd.setIdx(message.getSequence());
		cmd.setGtw(PlatformConnectStatus.getGatewayId());
		cmd.setDate(message.getRecvMsgDate());
		
		return cmd;
	}

	/**
	 * 将数据报文转换设备心跳指令
	 *
	 * @param message  数据报文
	 * @return
	 */
	public static DeviceHeartbeatCmd convertDeviceHeartCmdByMsg(Message message) {
		if (message == null) return null;

		DeviceHeartbeatCmd cmd = new DeviceHeartbeatCmd();
		//cmd.setCmd(0);
		cmd.setVer(0);
		cmd.setIdx(message.getSequence());
		cmd.setGtw(PlatformConnectStatus.getGatewayId());
		cmd.setDev(message.getDeviceNo());
		cmd.setDate(message.getRecvMsgDate());

		return cmd;
	}

	/**
	 * 将控制器状态报文转换设备(控制器)上传运行状态指令
	 *
	 * @param message 控制器状态报文
	 * @return
	 */
	public static ControllerStatusCmd convertCtrlStatusCmdByMsg(ControllerMessage message) {
		if (message == null) return null;

		ControllerStatusCmd cmd = new ControllerStatusCmd();
		cmd.setCmd(ConstantUtil.convertValueByEMessageType(message.getMsgType()));
		cmd.setVer(0);
		cmd.setIdx(SequenceUtil.generate());
		cmd.setGtw(PlatformConnectStatus.getGatewayId());
		cmd.setDev(message.getDeviceNo());
		cmd.setDate(message.getRecvMsgDate());
		
		if (message.getMsgType() == EMessageType.CTRL_ACTION) {
			cmd.setAck((int) message.getAck());
		}
		
		/*
		 * 暂时未启用
		if (message.getTag() != null) {
			cmd.setOri((Integer)message.getTag());
		}
		*/
		
		List<ControllerStatusItem> datas = new ArrayList<ControllerStatusItem>();
		for (ControllerMessageItem item : message.getItems()) {
			ControllerStatusItem statusItem = new ControllerStatusItem();
			
			statusItem.setAid(1);
			statusItem.setRid(item.getChannelNo());
			statusItem.setDevt(ConstantUtil.convertValueByEDeviceType(item.getDevType()));	//	设备类型
			statusItem.setSta(ConstantUtil.convertValueByEDeviceStatus(item.getDevStatus()));		//	设备状态
			statusItem.setVal(ConstantUtil.convertRunStatusByEDeviceStatus(item.getRunStatus()));	//	运行状态
			statusItem.setDate(item.getDate());
			
			statusItem.setVec(null);
			statusItem.setRate(null);
			statusItem.setSum(null);
			statusItem.setTime(null);
			
			datas.add(statusItem);
		}
		
		cmd.setData(datas);
		
		return cmd;
	}

	/**
	 * 根据通用回复报文转换为回复成功指令
	 *
	 * @param response  通用回复报文
	 * @return
	 */
	public static PlatformReplySuccessCmd convertFromResponseMessage(UniversalResponseMessage response) {
		PlatformReplySuccessCmd cmd = null;
		if (response.getRespCode() == EUniversalResponseCode.FAILED) {
			cmd = new PlatformReplyFailCmd();
		}
		else {
			cmd = new PlatformReplySuccessCmd();
		}
		
		cmd.setIdx(response.getSequence());
		cmd.setVer(0);
		cmd.setAck((int) response.getAck());
		cmd.setGtw(PlatformConnectStatus.getGatewayId());
		cmd.setDev(response.getDeviceNo());
		cmd.setDate(new Date());
		cmd.setCmd(ConstantUtil.convertValueByEMessageType(response.getMsgType()));
		
		/**
		 * 失败
		 */
		if (cmd instanceof PlatformReplyFailCmd) {
			((PlatformReplyFailCmd) cmd).setErr(true);
			((PlatformReplyFailCmd) cmd).setMsg(response.getMsg());
			((PlatformReplyFailCmd) cmd).setRetry(false);
		}
		
		return cmd;
	}


}
