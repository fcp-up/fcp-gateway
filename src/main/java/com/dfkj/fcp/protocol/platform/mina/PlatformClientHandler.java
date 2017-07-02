package com.dfkj.fcp.protocol.platform.mina;

import com.alibaba.fastjson.JSON;
import com.dfkj.fcp.core.vo.ControllerCommandMessage;
import com.dfkj.fcp.core.vo.Message;
import com.dfkj.fcp.protocol.hardware.factory.HardwareInstanceFactory;
import com.dfkj.fcp.protocol.platform.factory.PlatformInstanceFactory;
import com.dfkj.fcp.protocol.platform.util.CtrlCommandMsgUtil;
import com.dfkj.fcp.protocol.platform.vo.PlatformConnectStatus;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mina平台客户端处理器
 *
 * Created by JiangWenGuang on 2017/4/22.
 */
public class PlatformClientHandler extends IoHandlerAdapter{
	
	private static Logger logger = LoggerFactory.getLogger(PlatformClientHandler.class);

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {

		Message msg = (Message) message;
		/**
		 * 应答消息，暂时不做处理
		 */
		if (msg.getAck() > 0) {
			return;
		}

		/**
		 * 现在只支持下发控制命令
		 */
		if (msg instanceof ControllerCommandMessage) {

			logger.debug("PlatformClientHandler.messageReceived:"+ JSON.toJSONString(CtrlCommandMsgUtil.assemblyNormalMessage((ControllerCommandMessage)msg)));

			//	回复平台
			if(CtrlCommandMsgUtil.assemblyNormalMessage((ControllerCommandMessage)msg) != null){
				session.write(CtrlCommandMsgUtil.assemblyNormalMessage((ControllerCommandMessage)msg));
			}
			/**
			 * 将指令发送到平台Mina客户端上
			 * 然后再由平台Mina客户端发送物理设备上(在HardwareServerHandler实现)
			 */
			HardwareInstanceFactory.getMinaServerInstance().listenPlatformClientSendMessage(msg);

		}
		
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		PlatformConnectStatus.setConnectStatus(PlatformConnectStatus.CONNECT_DISCONNECT);
		//super.exceptionCaught(session, cause);
	}
}
