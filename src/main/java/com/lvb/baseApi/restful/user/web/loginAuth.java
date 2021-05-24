package com.lvb.baseApi.restful.user.web;

import com.alibaba.fastjson.JSON;
import com.lvb.baseApi.common.result.AjaxResult;
import com.lvb.baseApi.common.util.*;
import com.lvb.baseApi.restful.user.entity.AppUserEntity;
import com.lvb.baseApi.restful.user.service.AppUserService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping(value="/api/User/")
public class loginAuth {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public final static String getPageOpenidUrl = "https://api.weixin.qq.com/sns/jscode2session";
    public final static String appid = "wx4df8e5b4b3195190";
    public final static String secret = "3c7eb09050aa919bba23149bce19c7e1";

    @Resource
    private RedisUtils redisUtils;


    @Autowired
    private AppUserService appUserService;

    @RequestMapping("/login")
    @ResponseBody
    public String toLogin(String code,String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            //请求参数
            String params = "appid=" + appid + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
            String urlNameString = getPageOpenidUrl + "?" + params;
            JSONObject jsonObject = WeixinUtil.doGet(urlNameString);
            String wxOpenId = jsonObject.getString("openid");
            String wxSessionKey = jsonObject.getString("session_key");
            if (StringUtils.isNotBlank(wxOpenId) && StringUtils.isNotBlank(wxSessionKey)) {
                //然后根据openid去数据库判断有没有该用户信息，若没有则存入数据库，有则返回用户数据(这里由于没有数据库所以一致没有)
                AppUserEntity user = this.appUserService.getUserById(wxOpenId);
                if (user != null) {
                    //查询到用户信息，更新登录时间，并返回信息
                    user.setLastvisit_time(new Date());
                    this.appUserService.saveOrUpdate(user);
                } else {
                    AppUserEntity userInfo = new AppUserEntity();
                    userInfo.setId(IdWorker.getFlowIdWorkerInstance().nextId() + "");
                    userInfo.setOpenid(wxOpenId);
                    userInfo.setSession_key(wxSessionKey);
                    userInfo.setCreate_time(new Date());
                    userInfo.setLastvisit_time(new Date());
                    this.appUserService.saveOrUpdate(userInfo);
                };
                //根据token查询skey是否存在
                if(token!=null){
                    String skey_redis = redisUtils.get(token);
                    if(StringUtils.isNotBlank( skey_redis )){
                        //存在 删除 skey 重新生成skey 将skey返回
                        redisUtils.delete( skey_redis );
                    }
                }
                //生成token并添加到缓存
                String uuid = UUID.randomUUID().toString();
                redisUtils.set(uuid, jsonObject.getString("openid") + "&&" + jsonObject.getString("session_key"),Constant.TOKEN_EXPIRE);
                //这步应该set进实体类
                Map userInfo = new HashMap();
                userInfo.put("openId", wxOpenId);
                userInfo.put("token",uuid);
                map.put("data", userInfo);
                map.put("msg", "登陆成功");
                map.put("code", 200);
                return JSON.toJSONString(map);
            } else {
                map.put("msg", "未获取到用户openid 或 session");
                map.put("code", 202);
                return JSON.toJSONString(map);
            }
        } catch (Exception e) {
            map.put("status", "failed");
            map.put("msg", e.getMessage());
            return JSON.toJSONString(map);
        }
    }


    @PostMapping("/updateUserInfoLogin")
    public AjaxResult updateUserInfoLogin(String encryptedData, String iv, String token) {
        try{
            String[] openid_sessionKey=redisUtils.get(token).toString().split("&&");
            if(openid_sessionKey!=null&&openid_sessionKey.length>0){
                //解密获取用户信息
                JSONObject userInfoJSON=WechatGetUserInfoUtil.getUserInfo(encryptedData,openid_sessionKey[1],iv);
                if(userInfoJSON!=null){
                    AppUserEntity user = this.appUserService.getUserById(userInfoJSON.get("openId").toString());
                    if(user==null){
                        return AjaxResult.builder().code(202).msg("未查找到Openid:"+userInfoJSON.get("openId")).build();
                    };
                    user.setNick_name(userInfoJSON.get("nickName").toString());
                    user.setGender(Integer.parseInt(userInfoJSON.get("gender").toString()));
                    user.setCity(userInfoJSON.get("city").toString());
                    user.setProvince(userInfoJSON.get("province").toString());
                    user.setCountry(userInfoJSON.get("country").toString());
                    user.setAvatar_url(userInfoJSON.get("avatarUrl").toString());
                    this.appUserService.saveOrUpdate(user);
                    //这步应该set进实体类
                    Map userInfo = new HashMap();
                    userInfo.put("openId", userInfoJSON.get("openId"));
                    userInfo.put("nickName", userInfoJSON.get("nickName"));
                    userInfo.put("gender", userInfoJSON.get("gender"));
                    userInfo.put("city", userInfoJSON.get("city"));
                    userInfo.put("province", userInfoJSON.get("province"));
                    userInfo.put("country", userInfoJSON.get("country"));
                    userInfo.put("avatarUrl", userInfoJSON.get("avatarUrl"));
                    userInfo.put("createTime",new Date());
                    //userInfo.put("is_realname",user.getIs_realname());
                    userInfo.put("token",token);
                    // 解密unionId & openId;
                    if (userInfoJSON.get("unionId")!=null) {
                        userInfo.put("unionId", userInfoJSON.get("unionId"));
                    };
                    //然后根据openid去数据库判断有没有该用户信息，若没有则存入数据库，有则返回用户数据
                    Map<String,Object> dataMap = new HashMap<>();
                    dataMap.put("userInfo", userInfo);
                    return AjaxResult.builder().code(200).msg("授权成功").result(dataMap).build();
                }else{
                    return AjaxResult.builder().code(202).msg("授权失败").build();
                }
            }else{
                return AjaxResult.builder().code(202).msg("未获取到用户openid 或 session").build();
            }
        }catch (Exception e){
            return AjaxResult.builder().code(202).msg("token 解密失败").build();
        }
    }

    /**
     * 验证登录
     * @param wxToken
     * @return
     */
    @GetMapping("/validateLogin")
    public AjaxResult validateToken(String wxToken){
        Map<String,Object> dataMap = new HashMap<>();

        String[] obj= redisUtils.get(wxToken)==null?null:redisUtils.get(wxToken).toString().split("&&");
        if(obj!=null){
            redisUtils.set(wxToken,obj,Constant.TOKEN_EXPIRE);
            dataMap.put("isValid",true);
            return AjaxResult.builder().code(200).result(dataMap).build();
        }else{
            dataMap.put("isValid",false);
            return AjaxResult.builder().code(202).msg("验证失败").build();
        }
    }


//    /**
//         * @Description: 解密用户敏感数据
//         * @param encryptedData 明文,加密数据
//     * @param iv   加密算法的初始向量
//     * @return
//     */
//    @SuppressWarnings({ "unchecked", "rawtypes" })
//    @RequestMapping(value = "/decodeUserInfo", method = RequestMethod.POST)
//    @ResponseBody
//    public AjaxResult decodeUserInfo(String encryptedData, String iv, String token,String code) {
//        try {
//            if(!StringUtils.isNotBlank(code)){
//                return  AjaxResult.builder().code(202).msg("未获取到用户凭证code").build();
//            };
//            String params = "appid=" + appid + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
//            String urlNameString = getPageOpenidUrl + "?" + params;
//            JSONObject jsonObject = WeixinUtil.doGet(urlNameString);
//            String wxOpenId = jsonObject.getString("openid");
//            String wxSessionKey = jsonObject.getString("session_key");
//            if(wxOpenId==null || wxSessionKey ==null){
//                return AjaxResult.builder().code(202).msg("未获取到openid,code无效。").build();
//            };
//            JSONObject userInfoJSON = WechatGetUserInfoUtil.getUserInfo(encryptedData, wxSessionKey, iv);
//            if(userInfoJSON!=null){
//                //这步应该set进实体类
//                AppUserEntity user = this.appUserService.getUserById(userInfoJSON.get("openId").toString());
//                if(user==null){
//                    return AjaxResult.builder().code(202).msg("未查找到Openid:"+userInfoJSON.get("openId")).build();
//                };
//                user.setNick_name(userInfoJSON.get("nickName").toString());
//                user.setGender(Integer.parseInt(userInfoJSON.get("gender").toString()));
//                user.setCity(userInfoJSON.get("city").toString());
//                user.setProvince(userInfoJSON.get("province").toString());
//                user.setCountry(userInfoJSON.get("country").toString());
//                user.setAvatar_url(userInfoJSON.get("avatarUrl").toString());
//                this.appUserService.saveOrUpdate(user);
//                Map userInfo = new HashMap();
//                userInfo.put("openId", userInfoJSON.get("openId"));
//                userInfo.put("nickName", userInfoJSON.get("nickName"));
//                userInfo.put("gender", userInfoJSON.get("gender"));
//                userInfo.put("city", userInfoJSON.get("city"));
//                userInfo.put("province", userInfoJSON.get("province"));
//                userInfo.put("country", userInfoJSON.get("country"));
//                userInfo.put("avatarUrl", userInfoJSON.get("avatarUrl"));
//                // 解密unionId & openId;
//                if (userInfoJSON.get("unionId")!=null) {
//                    userInfo.put("unionId", userInfoJSON.get("unionId"));
//                };
//                //然后根据openid去数据库判断有没有该用户信息，若没有则存入数据库，有则返回用户数据
//                Map<String,Object> dataMap = new HashMap<>();
//                dataMap.put("userInfo", userInfo);
//                String uuid= UUID.randomUUID().toString();
//                dataMap.put("token", uuid);
//                redisTemplate.opsForValue().set(uuid,userInfo);
//                redisTemplate.expire(uuid, Constant.TOKEN_EXPIRE, TimeUnit.SECONDS);
//                return AjaxResult.builder().code(200).msg("登陆成功").result(dataMap).build();
//
//            }else{
//                return AjaxResult.builder().code(202).msg("解密失败").build();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


//    public String getAccessToken(String code ){
//        String accessToken  ="";
//        //未查询到用户信息，通过微信获取用户tokey信息
//        String requestUrl = GetPageAccessTokenUrl.replace("APPID", appid).replace("APPSECRET", secret);
//        JSONObject OpenidJSONO = WeixinUtil.doGet(requestUrl);
//        accessToken = String.valueOf(OpenidJSONO.get("access_token"));
//        return accessToken;
//    }
}
