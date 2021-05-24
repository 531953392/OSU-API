package com.lvb.baseApi.restful.login.adapter;

import com.lvb.baseApi.common.error.CommonErrorCode;
import com.lvb.baseApi.common.error.ErrorCodeException;
import com.lvb.baseApi.common.util.WeixinUtil;
import com.lvb.baseApi.restful.login.vo.SessionVo;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class WechatAdapter {
    private final Logger logger = LoggerFactory.getLogger(WechatAdapter.class);

    public final static String getPageOpenidUrl = "https://api.weixin.qq.com/sns/jscode2session";

    @Value("${wechat.appid}")
    private String appid;

    @Value("${wechat.secret}")
    private String secret;


    public SessionVo  jscode2session(String code) {
        try {
            logger.info("login-jscode2session-code:"+code);
            String params = "appid=" + appid + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
            String urlNameString = getPageOpenidUrl + "?" + params;
            JSONObject jsonObject = WeixinUtil.doGet(urlNameString);
            String wxOpenId = jsonObject.getString("openid");
            String wxSessionKey = jsonObject.getString("session_key");
            SessionVo sessionVo = new SessionVo();
            sessionVo.setOpenid(wxOpenId);
            sessionVo.setSessionKey(wxSessionKey);
            logger.info("login-jscode2session-wxOpenId:"+wxOpenId);
            logger.info("login-jscode2session-wxSessionKey:"+wxSessionKey);
            return sessionVo;
        } catch (Exception e) {
            logger.error("login-jscode2session-erroy:"+e.getMessage());
            throw new ErrorCodeException(CommonErrorCode.OBTAIN_OPENID_ERROR);
        }
    }
}
