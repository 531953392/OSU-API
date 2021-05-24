package com.lvb.baseApi.common.util;

import com.alibaba.fastjson.JSON;
import com.lvb.baseApi.common.entity.SmsSendRequest;
import com.lvb.baseApi.common.entity.SmsSendResponse;

public class SendMessage {
    public static String  account = "N2644232";
    public static  String password = "MCIEr91NUvaed7";
    public static final String charset = "utf-8";
    //请求地址请登录253云通讯自助通平台查看或者询问您的商务负责人获取
    public static  String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";

    public  static void send(String msg ,String phone){

        //状态报告
        String report= "true";

        SmsSendRequest smsSingleRequest = new SmsSendRequest(account, password, msg, phone,report);

        String requestJson = JSON.toJSONString(smsSingleRequest);

        System.out.println("before request string is: " + requestJson);

        String response = ChuangLanSmsUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);

        System.out.println("response after request result is :" + response);

        SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);

        System.out.println("response  toString is :" + smsSingleResponse);
    }
}
