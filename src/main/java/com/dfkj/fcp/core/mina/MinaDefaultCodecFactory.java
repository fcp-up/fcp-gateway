package com.dfkj.fcp.core.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.*;

/**
 * Mina FilterChain中默认添加的codec 作用为(注:在IFilterChain中起始处理的是byte[])
 * 	IoBuffer->byte[] byte[]->IoBuffer
 * 
 * @author songfei
 * @version 0.1
 */
public class MinaDefaultCodecFactory implements ProtocolCodecFactory {

	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return new ProtocolEncoder() {

			public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
				if (message != null && message instanceof byte[]) {
					byte[] bytes = (byte[])message;
					IoBuffer buffer = IoBuffer.allocate(bytes.length).setAutoExpand(true);
					buffer.put(bytes);
					buffer.flip();
					out.write(buffer);
				}
				else {
					
				}
			}

			public void dispose(IoSession session) throws Exception {
			}
			
		};
	}

	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		return new ProtocolDecoder() {

			public void decode(IoSession session, IoBuffer buffer, ProtocolDecoderOutput out) throws Exception {
				byte[] bytes = new byte[buffer.remaining()];
				buffer.get(bytes, 0, buffer.remaining());
				out.write(bytes);
			}

			public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {
			}

			public void dispose(IoSession session) throws Exception {
			}
			
		};
	}

}
