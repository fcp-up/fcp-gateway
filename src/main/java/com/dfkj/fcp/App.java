package com.dfkj.fcp;

import com.dfkj.fcp.config.SystemConfig;
import com.dfkj.fcp.core.logger.AcpLogger;
import com.dfkj.fcp.protocol.hardware.factory.HardwareInstanceFactory;

/**
 * 程序主入口
 */
public class App {

	private static AcpLogger logger = new AcpLogger(App.class);

	public static void main(String[] args) {
		SystemConfig.loadProps(args != null && args.length >0 ? args[0]:"");
		/*PlatformConnectionRunnable r = new PlatformConnectionRunnable();
		// 60*1000
		new Thread(r).start();*/
		try{
			HardwareInstanceFactory.newMinaServerInstance();
			HardwareInstanceFactory.getMinaServerInstance().startUp();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
