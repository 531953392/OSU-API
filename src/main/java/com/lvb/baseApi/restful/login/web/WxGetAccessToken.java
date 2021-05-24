package com.lvb.baseApi.restful.login.web;

import com.lvb.baseApi.common.result.AjaxResult;
import com.lvb.baseApi.common.util.SendMessage;
import com.lvb.baseApi.common.util.WeixinUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
public class WxGetAccessToken {

    public final static String getAccessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token";

    public final static String getSendMessageUrl = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send";

    @Value("${wechat.appid}")
    private String appid;

    @Value("${wechat.secret}")
    private String secret;


    /*
     * 获取getAccessToken
     * @return
     */
    @GetMapping("/api/getAccessToken")
    public AjaxResult getAccessToken() throws Exception {
        try {
            String params = "grant_type=client_credential"+"&appid="+appid+"&secret="+secret;
            String urlNameString = getAccessTokenUrl + "?" + params;
            JSONObject jsonObject = WeixinUtil.doGet(urlNameString);
            String access_token = jsonObject.getString("access_token");
            return AjaxResult.builder().code(200).result(access_token).msg("获取access_token成功").build();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /*
     * 发送推送模板消息，
     * @return
     */
    @RequestMapping(value="/api/sendMessage",method = RequestMethod.POST)
    public AjaxResult sendMessage(@RequestBody String jsonData) throws Exception {
        try {
            JSONObject model = JSONObject.fromObject(jsonData);
            String access_token = model.getString("access_token");
            String messageInfo = model.getString("messageInfo");

            String params = "access_token="+access_token;
            String urlNameString = getSendMessageUrl + "?" + params;
            JSONObject jsonObject = WeixinUtil.httpRequest(urlNameString,"POST",messageInfo);
            //发送短信通知
            //SendMessage.send("【优嗅云宠医】用户的订单已支付成功，请尽快进入小程序进行查看回复！","18010549761");
            return AjaxResult.builder().code(200).result(jsonObject).msg("发送成功").build();

        } catch (Exception e) {
            //throw new Exception(e);
            return AjaxResult.builder().code(203).msg(e.getMessage()).build();
        }
    }



    @RequestMapping(value = "/api/messageToken",method= RequestMethod.GET)
    public String checkWeixinValid(@RequestParam(name="signature")String signature,
                                   @RequestParam(name="timestamp")String timestamp,
                                   @RequestParam(name="nonce")String nonce,
                                   @RequestParam(name="echostr")String echostr) {
        return echostr;
    }
}
