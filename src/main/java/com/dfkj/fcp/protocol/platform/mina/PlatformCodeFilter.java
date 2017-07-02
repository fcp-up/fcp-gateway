package com.dfkj.fcp.protocol.platform.mina;

import com.alibaba.fastjson.JSON;
import com.dfkj.fcp.App;
import com.dfkj.fcp.core.constant.EMessageType;
import com.dfkj.fcp.core.logger.AcpLogger;
import com.dfkj.fcp.core.vo.*;
import com.dfkj.fcp.protocol.platform.util.CmdTransformMessageUtil;
import com.dfkj.fcp.protocol.platform.util.CtrlCommandMsgUtil;
import com.dfkj.fcp.protocol.platform.util.MsgTransformCmdUtil;
import com.dfkj.fcp.protocol.platform.util.SampleMessageRepositoryUtil;
import com.dfkj.fcp.protocol.platform.vo.*;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.future.DefaultWriteFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.DefaultWriteRequest;
import org.apache.mina.core.write.WriteRequest;

import java.util.Date;
import java.util.List;

/**
 * Created by JiangWenGuang on 2017/4/20.
 */
public class PlatformCodeFilter extends IoFilterAdapter {

    private static AcpLogger logger = new AcpLogger(App.class);

    @Override
    public void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws Exception {
        if (message instanceof byte[]) {
            ControllerCommandMessage controlMsg = this.decode(session, message);
            if(controlMsg != null) {
                nextFilter.messageReceived(session, controlMsg);
            }
        }else{
            super.messageReceived(nextFilter, session, message);
        }

    }

    @Override
    public void filterWrite(NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
        if (writeRequest.getMessage() instanceof Message) {
            String json = this.encode(session, writeRequest.getMessage());
            WriteFuture writeFuture = new DefaultWriteFuture(session);
            WriteRequest _writeRequest = new DefaultWriteRequest(json.getBytes(), writeFuture, null);
            nextFilter.filterWrite(session, _writeRequest);

        }

    }

    /**
     * 解析平台发送过来的指令
     *
     * @param session
     * @param data
     */
    public ControllerCommandMessage decode(IoSession session, Object data) {

        if (!(data instanceof byte[])) {
            return null;
        }

        String text = new String((byte[]) data);
        Cmd cmd = JSON.parseObject(text, Cmd.class);
        if (cmd == null) {
            logger.debug("解析失败,原内容为:\n" + text);
            return null;
        }

        logger.debug("解析:\n" + cmd.toString());
        logger.debug("平台命令 :"+ cmd.toString());

        if (cmd.getCmd() == 6) {
            /**
             * 控制设备命令，
             */
            ControllerCommandCmd actionCmd = JSON.parseObject(text, ControllerCommandCmd.class);
            if (actionCmd == null || actionCmd.isAll()) {

                logger.info("当前版本暂时不支持all控制");

                ControllerCommandMessage actionMessage = new ControllerCommandMessage();
                actionMessage.setSequence(cmd.getIdx());
                actionMessage.setDeviceNo(actionMessage.getDeviceNo());

                session.write(CtrlCommandMsgUtil.assemblyErrorMessage(actionMessage, "不支持批量操作"));
            }
            else {
                List<ControllerCommandMessage> actions = CmdTransformMessageUtil.convertFromControllerCommandSpecCmd(
                        JSON.parseObject(text, ControllerCommandSpecCmd.class));
                if (actions.size() > 1) {
                    session.write(CtrlCommandMsgUtil.assemblyErrorMessage(actions.get(0), "不支持批量操作"));
                }
                else {
                    if (actions.size() == 1) {
                        return actions.get(0);
                    }
                }
            }
        }else if(cmd.getCmd() == 0){
             PlatformConnectStatus.setHeartTime(new Date().getTime());
        }else  {

        }

        return null;

    }

    /**
     * 将数据编码成指令发送到平台
     *
     * @param session
     * @param message
     * @return
     */
    public String encode(IoSession session, Object message) {

        if (!(message instanceof Message)) {
            return "";
        }

        Message msg = (Message)message;
        if (msg.getRecvMsgDate() == null) {
            msg.setRecvMsgDate(new Date());
        }

        /**
         * 保存设备的状态
         */
        if (message instanceof DataMessage) {
            DataMessage dataMessage = (DataMessage)message;
            for (DataMessageItem item : dataMessage.getData()) {
                SampleMessageRepositoryUtil.getInstance().save(item.hashCode(), item);
            }
        }
        else if (message instanceof ControllerMessage) {
            ControllerMessage ctrlMessage = (ControllerMessage)message;
            for (ControllerMessageItem item : ctrlMessage.getItems()) {
                SampleMessageRepositoryUtil.getInstance().save(item.hashCode(), item);
            }
        }

        logger.debug("msgType:"+msg.getMsgType());
        logger.debug("msg:"+JSON.toJSONString(msg));

        Cmd cmd = null;
        if (msg.getMsgType() == EMessageType.SENSOR_DATA) {
            cmd = MsgTransformCmdUtil.convertUpDataCmdByMsg((DataMessage)msg);
            PlatformMinaClient.addDevice(msg.getDeviceNo());
        }
        else if (msg.getMsgType() == EMessageType.GATEWAY_HEARTBEAT) {
            cmd = MsgTransformCmdUtil.convertGatewayHeartCmdByMsg(msg);
        }
        else if (msg.getMsgType() == EMessageType.HEARTBEAT) {
            cmd = MsgTransformCmdUtil.convertDeviceHeartCmdByMsg(msg);
        }
        else if (msg instanceof ControllerMessage) {
            /**
             * 因下发控制指令的回复与设备的状态汇报都使用ControllerMessage，只是MessageType不一样
             */
            cmd = MsgTransformCmdUtil.convertCtrlStatusCmdByMsg((ControllerMessage)msg);
        }
        else if (msg instanceof UniversalResponseMessage) {
            cmd = MsgTransformCmdUtil.convertFromResponseMessage((UniversalResponseMessage) msg);
        }
        else {
            logger.debug("现暂时只支持传感器数据上传.");
        }

        logger.debug("cmd:"+JSON.toJSONString(cmd));

        if (cmd != null) {
            String json = "";
            if (cmd instanceof ControllerStatusCmd) {
                /**
                 * 过滤多余的字段
                 * ack字段在状态汇报中不使用
                 */
                ControllerStatusCmd ctrlCmd = (ControllerStatusCmd)cmd;
                json = JSON.toJSONString(ctrlCmd);
            }
            else {
                json = JSON.toJSONString(cmd);
            }
            logger.debug("上传平台数据：+++++++++++++++++++++"+JSON.toJSONString(cmd));
            return json;
        }
        return "";
    }

}
