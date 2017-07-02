package com.dfkj.fcp.protocol.hardware.mina;



import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.future.DefaultWriteFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.DefaultWriteRequest;
import org.apache.mina.core.write.WriteRequest;

import com.dfkj.fcp.core.constant.EUniversalResponseCode;
import com.dfkj.fcp.core.logger.AcpLogger;
import com.dfkj.fcp.core.util.ByteArray;
import com.dfkj.fcp.core.util.HexUtil;
import com.dfkj.fcp.core.vo.Message;
import com.dfkj.fcp.core.vo.UniversalResponseMessage;
import com.dfkj.fcp.protocol.hardware.util.Protocol;
import com.dfkj.fcp.protocol.hardware.util.ProtocolAgri;
import com.dfkj.fcp.protocol.hardware.util.ProtocolAlarm;
import com.dfkj.fcp.protocol.hardware.util.ProtocolContext;

/**
 * 硬件报文编码处理
 *
 * Created by JiangWenGuang on 2017/4/22.
 */
public class HardwareCodeFilter extends IoFilterAdapter {

    private final static AcpLogger logger = new AcpLogger(HardwareCodeFilter.class);

    private Gtw1P1Decode decode = null;
    private Gtw1P1Encode encode = null;

    public HardwareCodeFilter() {
        decode = new Gtw1P1Decode();
        encode = new Gtw1P1Encode();
    }

    @Override
    public void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws Exception {
        if (message instanceof byte[]) {
            Message msg = decode.decode(session, new ByteArray((byte[])message));
            nextFilter.messageReceived(session, msg);
        }else{
            super.messageReceived(nextFilter, session, message);
        }

    }

    @Override
    public void filterWrite(NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
        if (writeRequest.getMessage() instanceof Message) {
            byte [] bytes = encode.encode(session, (Message)writeRequest.getMessage());
            WriteFuture writeFuture = new DefaultWriteFuture(session);
            WriteRequest _writeRequest = new DefaultWriteRequest(bytes, writeFuture, null);
            nextFilter.filterWrite(session, _writeRequest);
        }else{
            super.filterWrite(nextFilter, session, writeRequest);
        }

    }

    /**
     * 解码
     * @author songfei
     *
     */
    private class Gtw1P1Decode {
    	Protocol prototol;
    	Message message;
        public Message decode(IoSession session, Object data) {
            if (!(data instanceof ByteArray)) {
                return null;
            }
            ByteArray byteArray = (ByteArray)data;
            logger.debug("Gtw1P1Decode decode");
            logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            logger.debug("server收到的包:\n" + HexUtil.ByteToString(byteArray.toBytes(), " "));
            logger.info("收到的包:"+HexUtil.ByteToString(byteArray.toBytes(), " "));

            //解包
            if (byteArray.getBeginByte() == (byte) 0x7E || byteArray.getLastByte() == (byte) 0x7E) {
            	prototol = new ProtocolAgri(); 
            	if(!prototol.isCurrentProtocol(byteArray)){
            		logger.info("无效的数据包.");
            		return null;
            	}
			}else{
				prototol = new ProtocolAlarm();
				if(!prototol.isCurrentProtocol(byteArray)){
					logger.info("无效的数据包.");
            		return null;
				}
			}
            ProtocolContext pctx = new ProtocolContext(prototol);
        	message = pctx.unpack(byteArray); 
        	
            if (message == null) {
                logger.debug("解包失败.");
                return null;
            }
            
            logger.debug("Message server :" + message.toString());

            //	TODO 处理分包

            //	解析数据
            message = prototol.parse(message, byteArray);
            /**
             * 应答标志位：为应答的则不需要进一步处理
             * TODO 消息的回复需移到到handler中
             * TODO 消息的过滤需移到单独的filter中
             */
            if (message.getAck() < 0) {
                //	回应数据包
                UniversalResponseMessage responseMessage = UniversalResponseMessage.create(message, EUniversalResponseCode.OK);
                session.write(responseMessage);
                return message;
            }

            return null;
        }

    }

    /**
     * 编码
     * @author songfei
     *
     */
    private class Gtw1P1Encode {
    	Protocol protocol;
        public byte [] encode(IoSession session, Message message) {
            logger.debug("Gtw1P1Encode encode.");
            ByteArray byteArray = protocol.pack(message); 
            if (byteArray != null) {
                logger.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                return byteArray.toBytes();
            }
            return null;
        }

    }
}
