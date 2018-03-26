package com.nfet.icare.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @ClassName:  AppIdAndSecretUtil   
 * @Description:获取配置文件(wechat.properties)中设置的appid和appsecret,
 *              需要修改成(测试)公众号中的数据
 * @author: zerods
 * @date:   2017年5月31日 
 * @version V1.0   
 * @Copyright: 2017 www.xxx.com Inc. All rights reserved.
 */
public class AppIdAndSecretUtil {
    private static String appid;
    private static String secret;
    
    static {
        Properties properties = new Properties();
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(
                "application.properties");  
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        appid = properties.getProperty("appid");
        secret= properties.getProperty("secret");
    }
    
    public static String getAppId() {
        return appid;
    }
    
    public static String getSecret() {
        return secret;
    }
    
    public static void main(String[] args) {
        System.out.println("appid: " + appid);
        System.out.println("secret: " + secret);
    }
}
