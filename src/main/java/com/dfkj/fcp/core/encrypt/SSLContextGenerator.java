package com.dfkj.fcp.core.encrypt;

import com.dfkj.fcp.config.SystemConfig;
import com.dfkj.fcp.core.logger.AcpLogger;

import org.apache.mina.filter.ssl.KeyStoreFactory;
import org.apache.mina.filter.ssl.SslContextFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;

/**
 * SSLContextGenerator
 * @author songfei
 * @date 2016-06-20
 */
public class SSLContextGenerator {

	private static AcpLogger logger = new AcpLogger(SSLContextGenerator.class);
	
	public SSLContext getSslContext() {
		SSLContext sslContext = null;
		try {
			byte[] serverData = readData("resources/certificates/GatewayServer.jks");
			if (serverData == null) {
				serverData = readData("certificates/GatewayServer.jks");
			}
			if (serverData == null) {
				logger.debug("获取Server jks失败");
			}
			
			byte[] gtwData = readData("resources/certificates/GatewayRemote.jks");
			if (gtwData == null) {
				gtwData = readData("certificates/GatewayRemote.jks");
			}
			if (gtwData == null) {
				logger.debug("获取Gtw jks失败");
			}

			if ((serverData != null && serverData.length > 0) && (gtwData != null && gtwData.length > 0)) {
				final KeyStoreFactory keyStoreFactory = new KeyStoreFactory();
				keyStoreFactory.setData(gtwData);
				keyStoreFactory.setPassword("Ysyh_l9t");

				final KeyStoreFactory trustStoreFactory = new KeyStoreFactory();
				trustStoreFactory.setData(serverData);
				trustStoreFactory.setPassword("Flzx_3kc");

				final SslContextFactory sslContextFactory = new SslContextFactory();
				final KeyStore keyStore = keyStoreFactory.newInstance();
				sslContextFactory.setKeyManagerFactoryKeyStore(keyStore);

				final KeyStore trustStore = trustStoreFactory.newInstance();
				sslContextFactory.setTrustManagerFactoryKeyStore(trustStore);
				sslContextFactory.setKeyManagerFactoryKeyStorePassword("Ysyh_l9t");
				sslContext = sslContextFactory.newInstance();
				
				logger.info("SSL provider is: " + sslContext.getProvider());
				
			} else {
				logger.info("Keystore or Truststore file does not exist");
			}
		} catch (Exception e) {
			logger.error("Generator SSLContext Error" + e.getMessage());
		}
		return sslContext;
	}
	
	protected byte[] readData(String filePath) {
		byte[] byteData = null;
		InputStream inStream = null;
		try {
			
			inStream = SSLContextGenerator.class.getClassLoader().getResourceAsStream(filePath);
			if (inStream.available() > 0) {
				byteData = new byte[inStream.available()];
				inStream.read(byteData);
			}
		} catch (Exception e) {
			//logger.debug("获取jks失败 " + e.getMessage());
			byteData = null;
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
				}
			}
		}
		return byteData;
	} 
}
