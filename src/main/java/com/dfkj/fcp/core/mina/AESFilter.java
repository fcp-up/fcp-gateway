package com.dfkj.fcp.core.mina;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.DefaultWriteRequest;
import org.apache.mina.core.write.WriteRequest;

import com.dfkj.fcp.core.util.AESUtil;

/**
 * AES加解密过滤器
 *
 * Created by JiangWenGuang on 2017/4/21.
 */
public class AESFilter extends IoFilterAdapter {

    @Override
    public void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws Exception {
        if (message != null && message instanceof byte[]) {
            String msg = new String((byte[])message);
            msg = AESUtil.decrypt(msg);
            super.messageReceived(nextFilter, session, msg.getBytes());
        } else {
            super.messageReceived(nextFilter, session, message);
        }
    }

    @Override
    public void filterWrite(NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
        if (writeRequest.getMessage() != null && writeRequest.getMessage()  instanceof byte[]) {
            String msg = new String((byte[])writeRequest.getMessage());
            msg = AESUtil.encrypt(msg);
            WriteRequest newWriteRequest = new DefaultWriteRequest(msg.getBytes(), writeRequest.getFuture(), writeRequest.getDestination());
            super.filterWrite(nextFilter, session, newWriteRequest);
        } else {
            super.filterWrite(nextFilter, session, writeRequest);
        }
    }
}
