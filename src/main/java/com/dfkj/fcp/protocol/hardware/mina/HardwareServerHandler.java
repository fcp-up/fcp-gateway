package com.dfkj.fcp.protocol.hardware.mina;

import java.util.Date;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import com.dfkj.fcp.core.logger.AcpLogger;
import com.dfkj.fcp.core.util.FormatUtil;
import com.dfkj.fcp.core.vo.Message;
import com.dfkj.fcp.protocol.hardware.factory.HardwareInstanceFactory;

/**
 * Mina硬件服务端处理器
 *
 * Created by JiangWenGuang on 2017/4/22.
 */
public class HardwareServerHandler extends IoHandlerAdapter {

    private final static AcpLogger logger = new AcpLogger(HardwareServerHandler.class);

    public void messageReceived(IoSession session, Object message) throws Exception {

        logger.debug("HardwareServerHandler.messageReceived:" + message.getClass().toString() + " " + FormatUtil.DATE_FORMAT.format(new Date()));

       /* if (message instanceof DataMessage) {
            DataMessage dataMessage = (DataMessage) message;
        }*/
        
        /**
         * 将处理完的数据转换格式同步发给平台Mina客户端
         * 然后又平台Mina客户端发生消息至平台(适配器项目)
         */
       /* PlatformInstanceFactory.getMinaClientInstance().listenHardwareServerSendMessage((Message)message);*/

		/*
		 * 获取当前设备需要下发的Message Queue 目前只支持控制器
		 */
        /*if (message instanceof ControllerMessage) {
            ControllerMessage ctrlMessage = (ControllerMessage) message;
            for (ControllerMessageItem item : ctrlMessage.getItems()) {
                while (HardwareInstanceFactory.getMinaServerInstance().hasMessage(item.hashCode())){
                    logger.debug("HardwareServerHandler.messageReceived 下发指令至物理设备上");
                    Message msg = HardwareInstanceFactory.getMinaServerInstance().take(item.hashCode());
                    session.write(msg);
                }
            }
        }*/

    }

}
