package com.dfkj.fcp.protocol.hardware.mina;

import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.future.DefaultWriteFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.DefaultWriteRequest;
import org.apache.mina.core.write.WriteRequest;

import com.dfkj.fcp.core.logger.AcpLogger;
import com.dfkj.fcp.core.util.ByteArray;
import com.dfkj.fcp.core.util.HexUtil;
import com.dfkj.fcp.core.vo.HelloMessage;
import com.dfkj.fcp.core.vo.Message;
import com.dfkj.fcp.protocol.hardware.factory.HardwareInstanceFactory;
import com.dfkj.fcp.protocol.hardware.util.ProtocolUtil;

/**
 * 硬件报文编码处理
 *
 * Created by JiangWenGuang on 2017/4/22.
 */
public class HardwareCodeFilter extends IoFilterAdapter {

    private final static AcpLogger logger = new AcpLogger(HardwareCodeFilter.class);

    private Gtw1P1Decode decode = null;
    private Gtw1P1Encode encode = null;
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	List<Message> listMsg = new ArrayList();

    public HardwareCodeFilter() {
        decode = new Gtw1P1Decode();
        encode = new Gtw1P1Encode();
    }

    @Override
    public void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws Exception {
        if (message instanceof byte[]) {
        	List<Message> msg = decode.decode(session, new ByteArray((byte[])message));
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
    	//应答帧为 FE FE FE
    	byte[] response = new byte[]{(byte)0xFE,(byte)0xFE,(byte)0xFE};
        public List<Message> decode(IoSession session, Object data) {
            if (!(data instanceof ByteArray)) {
                return null;
            }
            ByteArray recPack = (ByteArray)data;
            logger.debug("Gtw1P1Decode decode");
            logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            logger.debug("server收到的包:\n" + HexUtil.ByteToString(recPack.toBytes(), " "));
            logger.info("收到的包:"+HexUtil.ByteToString(recPack.toBytes(), " "));            
            int recPackLen = recPack.size();
            int lessPackLen = recPackLen;
            while(lessPackLen >= 3){
            	if(recPack.getAt(0) == (byte) 0xAA && recPack.getAt(1) == (byte)0xAA 
            			&& recPack.getAt(2) == (byte)0xAA){
            		//根据session打印终端信息
            		HelloMessage msg =  HardwareInstanceFactory.getMinaServerInstance().terminalsMap.get(session.getId());
            		if(null != msg){
            			logger.info("平台收到集中器【" + msg.getCenterNo() + "】的心跳包.");
            		}
            		//如果是心跳包或握手包则立即回应
                    session.write(response); 
                    lessPackLen -= 3; 
                    recPack.removeAt(0,3);
            	}else if(recPack.getBeginByte() == (byte)0x7E && recPack.size() >= 19){
            		//解包            
            		if(recPack.getAt(18) == (byte)0x7E){ 
            			ByteArray tmp = new ByteArray(recPack.subByteArray(recPack,0,18));
            			Message message = ProtocolUtil.unpack(tmp);
                        if (message == null) {
                            logger.debug("解包失败.");
                        }else{
                        	logger.debug("Message server :" + message.toString());
                            //解析数据
                            message = ProtocolUtil.parse(message, tmp);
                            listMsg.add(message);
                            session.write(response);
                        }
            		}                    
            		lessPackLen -= 19;
                    recPack.removeAt(0,19);  
            	}else{
            		//移除不完整的包
            		recPack.removeAt(0); 
            		lessPackLen -= 1;
            	}            	
            }          
           return listMsg;           
        }
    }

    /**
     * 编码
     * @author songfei
     *
     */
    private class Gtw1P1Encode {

        public byte [] encode(IoSession session, Message message) {
            logger.debug("Gtw1P1Encode encode.");

            ByteArray byteArray = ProtocolUtil.pack(message);
            if (byteArray != null) {
                logger.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                return byteArray.toBytes();
            }
            return null;
        }

    }
}
