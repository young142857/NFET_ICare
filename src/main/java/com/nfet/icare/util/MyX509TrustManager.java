package com.nfet.icare.util;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * @ClassName:  MyX509TrustManager   
 * @Description:信任证书管理器
 * @author: zerods
 * @date:   2017年5月22日 
 * @version V1.0   
 * @Copyright: 2017 www.xxx.com Inc. All rights reserved.
 */
public class MyX509TrustManager implements X509TrustManager {

    // 检查客户端证书
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        
    }
    
    // 检查服务器端证书
    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        
    }

    // 返回受信任的X509证书数组
    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
    
}
