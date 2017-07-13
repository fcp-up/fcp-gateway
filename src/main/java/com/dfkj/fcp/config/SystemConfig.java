package com.dfkj.fcp.config;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Properties;

import com.dfkj.fcp.core.logger.AcpLogger;

/**
 * 系统配置
 *
 * Created by JiangWenGuang on 2017/4/20.
 */
public class SystemConfig {

    private static AcpLogger logger = new AcpLogger(SystemConfig.class);

    // 属性文件名
    private static final String DEFAULT_FILE = "application.properties";
    /**
     * 网关idKey
     */
    public static final String GATEWAY_ID_KEY= "gateway.id";
    /**
     * 物理设备名称Key
     */
   // public static final String DEVICE_NAME_KEY= "device.name";
    /**
     * 物理设备Ip地址Key
     */
    public static final String DEVICE_ADDRESS_KEY= "device.address";
    /**
     * 物理设备端口Key
     */
    public static final String DEVICE_PORT_KEY= "device.port";
    /**
     * 是否开启自动发送设备报文Key
     */
    public static final String PLATFORM_AUTO_SEND_DEVICE_MESSAGE_KEY= "platform.auto.send.device.message";
    /**
     * 设备没有应答超时时间Key
     */
    public static final String PLATFORM_DEVICE_NO_RESPONSE_TIMEOUT_KEY= "platform.device.no.response.timeout";
    /**
     * 离线存储Key
     */
    public static final String PLATFORM_OFFLINE_STORAGE_KEY= "platform.offline.storage";
    /**
     * RabbitMq ip地址Key
     */
    public static final String RABBITMQ_HOST_KEY= "spring.rabbitmq.host";
    /**
     * RabbitMq 端口Key
     */
    public static final String RABBITMQ_PORT_KEY= "spring.rabbitmq.port";
    /**
     * RabbitMq 用户名Key
     */
    public static final String RABBITMQ_USERNAME_KEY= "spring.rabbitmq.username";
    /**
     * RabbitMq 密码Key
     */
    public static final String RABBITMQ_PASSWORD_KEY= "spring.rabbitmq.password";
    /**
     * 传感器上传数据解析规则
     */
    public static final String DEVICE_UPDATA_PARSE_RULE_KEY= "device.updata.parse.rule";

    /**
     * 离线存储:开启
     */
    public static final String PLATFORM_OFFLINE_STORAGE_OPEN = "1";
    /**
     * 自动发送设备报文:开启
     */
    public static final String PLATFORM_AUTO_SEND_DEVICE_MESSAGE_OPEN = "1";

    private static Properties props;

    /**
     * 加载属性文件
     * @param filename
     */
    synchronized static public void loadProps(String filename){

        logger.info("开始加载properties文件内容.......");
        props = new Properties();
        InputStream in = null;

        try {
            if(null == filename || "".equals(filename)){
                String codeSourcePath = SystemConfig.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            	codeSourcePath = URLDecoder.decode(codeSourcePath, Charset.defaultCharset().displayName());
                filename = codeSourcePath + DEFAULT_FILE;
            	//System.out.println(System.getProperty("user.dir") + "\\"+DEFAULT_FILE);
            	InputStream ips = SystemConfig.class.getResourceAsStream("/application.properties");
            	BufferedReader ipss = new BufferedReader(new InputStreamReader(ips));
            	props.load(ipss);
            }

            /*File file = new File(filename);
            System.out.println(file.exists());
            in = new BufferedInputStream(new FileInputStream(file.getPath()));
            props.load(in);*/

        } catch (FileNotFoundException e) {
            logger.error("属性文件没找到文件未找到");
        } catch (IOException e) {
            logger.error("出现IOException");
        } finally {
            try {
                if(null != in) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error("文件流关闭出现异常");
            }
        }

        logger.info("加载properties文件内容完成...........");
        logger.info("properties文件内容：" + props);

    }

    /**
     * 获取属性
     * @param key
     * @return
     */
    public static String getProperty(String key){

        return props == null ? null :  props.getProperty(key);

    }

    /**
     * 获取属性
     * @param key 属性key
     * @param defaultValue 属性value
     * @return
     */
    public static String getProperty(String key,String defaultValue){

        return props == null ? null : props.getProperty(key, defaultValue);

    }

}
