package com.dfkj.fcp.protocol.platform.util;


import com.dfkj.fcp.core.constant.*;
import com.dfkj.fcp.core.util.PlusList;
import com.dfkj.fcp.core.util.SequenceUtil;
import com.dfkj.fcp.core.vo.*;

import java.util.Date;

/**
 * 控制器控制命令报文转换
 *
 * @Author JiangWenGuang
 */
public class CtrlCommandMsgUtil {

	/**
	 * 组装正常报文
	 *
	 * @param actionMessage 控制器控制命令报文
	 * @return
	 */
	public static Message assemblyNormalMessage(ControllerCommandMessage actionMessage) {

		ControllerMessage ctrlMessage = new ControllerMessage();

		if (actionMessage.getAction() == EAction.NO_ACTION) {
			return assemblyErrorMessage(actionMessage, "不支持的操作.");
		}

		ctrlMessage.setSequence(SequenceUtil.generate());
		ctrlMessage.setAck(actionMessage.getSequence());
		ctrlMessage.setId(actionMessage.getId());
		ctrlMessage.setDeviceNo(actionMessage.getDeviceNo());
		ctrlMessage.setRecvMsgDate(new Date());
		ctrlMessage.setDevCategory(EDeviceCategory.CONTROLLER);
		ctrlMessage.setPtyLvl(EPriorityLvl.HIGH); // 默认为高优先级
		ctrlMessage.setVersion(0);

		/**
		 * 注意这是使用的是CTRL_ACTION类型，为了转为CMD时设置cmd值使用
		 */
		ctrlMessage.setMsgType(EMessageType.CTRL_ACTION);

		PlusList<ControllerMessageItem> items = new PlusList<ControllerMessageItem>();
		{
			/**
			 * 获取设备状态
			 */
			ControllerMessageItem item = (ControllerMessageItem)SampleMessageRepositoryUtil.getInstance().getById(actionMessage.hashCode());
			if (item == null) {
				return null;
			}

			items.add((ControllerMessageItem)item.clone());
		}
		ctrlMessage.setItems(items);

		return ctrlMessage;
	}

	/**
	 * 组装错误的通用报文
	 *
	 * @param actionMessage 控制器控制命令报文
	 * @param msg 通用报文
	 * @return
	 */
	public static UniversalResponseMessage assemblyErrorMessage(ControllerCommandMessage actionMessage, String msg) {
		UniversalResponseMessage message = new UniversalResponseMessage();

		message.setVersion(0);
		message.setSequence(SequenceUtil.generate());
		message.setAck(actionMessage.getSequence());
		message.setMsgType(EMessageType.CTRL_ACTION);
		message.setDeviceNo(actionMessage.getDeviceNo());
		message.setRespDate(new Date());
		message.setRespCode(EUniversalResponseCode.FAILED);
		message.setMsg(msg);
		
		return message;
	}
}
