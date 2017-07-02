package com.dfkj.fcp.core.mina;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.future.DefaultWriteFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.DefaultWriteRequest;
import org.apache.mina.core.write.WriteRequest;

import java.util.ArrayList;

/**
 * 加解码'\n'的报文
 *
 * Created by JiangWenGuang on 2017/4/21.
 */
public class SimpleTextLineCodecFilter extends IoFilterAdapter {

    @Override
    public void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws Exception {
        ArrayList<byte[]> list = this.decode(session, message);
        for(byte [] bytes : list){
            nextFilter.messageReceived(session, bytes);
        }
    }

    @Override
    public void filterWrite(NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
        Object msg = encode(session, writeRequest.getMessage());
        WriteFuture writeFuture = new DefaultWriteFuture(session);
        WriteRequest _writeRequest = new DefaultWriteRequest(msg, writeFuture, null);
        nextFilter.filterWrite(session, _writeRequest);
    }

    /**
     * 解码
     *
     * @param session
     * @param data
     * @return
     */
    public ArrayList<byte[]>  decode(IoSession session, Object data) {
        if (!(data instanceof byte[])) {
            return null;
        }

        byte[] bytes = (byte[])data;
        ArrayList<byte[]> list = new ArrayList<>();
        int textLength = 0;
        for (int idx = 0; idx < bytes.length; idx++) {
            if (bytes[idx] == '\n') {
                if (textLength > 0) {
                    byte[] tmp = new byte[textLength];
                    System.arraycopy(bytes, idx - textLength, tmp, 0, textLength);
                    list.add(tmp);
                }
                textLength = 0;
            }
            textLength += 1;
        }
        return list;
    }

    /**
     * 编码
     *
     * @param session
     * @param message
     * @return
     */
    public Object encode(IoSession session, Object message) {
        if (message instanceof byte[]) {
            byte[] bytes = (byte[])message;
            byte[] tmp = new byte[bytes.length + 1];
            System.arraycopy(bytes, 0, tmp, 0, bytes.length);
            tmp[bytes.length] = '\n';
            return tmp;
        }
        else {
            return message;
        }
    }
}
