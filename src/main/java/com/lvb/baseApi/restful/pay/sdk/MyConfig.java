package com.lvb.baseApi.restful.pay.sdk;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @Description: 微信支付配置类
 */
public class MyConfig extends WXPayConfig {

    private byte[] certData;
    public MyConfig() throws Exception {
//        String certPath = "classpath:apiclient_cert.p12";
//        File file = new File(certPath);
//        InputStream certStream = new FileInputStream(file);
//        this.certData = new byte[(int) file.length()];
//        certStream.read(this.certData);
//        certStream.close();
    }

    @Override
    public String getAppID() {
        return "wx50834869450623d9";
    }

    @Override
    public String getMchID() {
        return "1603107591";
    }

    @Override
    public String getKey() {
        return "youxiudeyouxiuyunchongyi20200930";
    }

    @Override
    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 10000;
    }

    @Override
    IWXPayDomain getWXPayDomain() {
        return new IWXPayDomain() {
            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {
            }

            @Override
            public DomainInfo getDomain(WXPayConfig config) {
                return new DomainInfo("api.mch.weixin.qq.com", false);
            }
        };
    }
}
