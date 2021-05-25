package com.lvb.baseApi.restful.login.web;

import com.lvb.baseApi.common.result.AjaxResult;
import com.lvb.baseApi.common.util.*;
import com.lvb.baseApi.restful.login.adapter.WechatAdapter;
import com.lvb.baseApi.restful.login.vo.LoginVo;
import com.lvb.baseApi.restful.login.vo.SessionVo;
import com.lvb.baseApi.restful.login.vo.UserInfoVo;
import com.lvb.baseApi.restful.user.entity.AppUserEntity;
import com.lvb.baseApi.restful.user.service.AppUserService;
import net.sf.json.JSONObject;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@RestController

public class WXAppletUserInfo {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WechatAdapter wechatAdapter;

    @Autowired
    private AppUserService appUserService;

    @Resource
    private RedisUtils redisUtils;

    /**
     * 获取微信小程序 session_key 和 openid
     */
    public AjaxResult getSessionKeyAndOropenid(String code){
        //微信端登录code值
        String wxCode = code;
        //获取openid和session_key
        SessionVo sessionVo=wechatAdapter.jscode2session(wxCode);
        return AjaxResult.builder().code(200).msg("OK").result(sessionVo).build();
    }
    /*validateLogin
     *  获取小程序用户基本信息
     */
    @RequestMapping("/api/login")
    public AjaxResult getUserInfo(@RequestBody LoginVo loginVo) throws UnsupportedEncodingException {
        //微信端登录code值
        String wxCode = loginVo.getCode();
        //获取openid和session_key
        SessionVo sessionVo=wechatAdapter.jscode2session(wxCode);
        AppUserEntity old_user = this.appUserService.getUserById(sessionVo.getOpenid());
        UserInfoVo userInfoVo=loginVo.getUserInfo() ;
        AjaxResult ajaxResult = GetMobile(loginVo.getEncryptedData(),loginVo.getIv(),sessionVo.getSessionKey());
        if(ajaxResult.code!=200){
            return AjaxResult.builder().code(202).msg(ajaxResult.msg).build();
        }
        String  mobile = ajaxResult.getData().toString();
        if (old_user != null) {
            logger.info("查询到用户信息，更新登录时间，并返回信息");
            old_user.setLastvisit_time(new Date());
            old_user.setNick_name(old_user.getNick_name()==null?mobile:EmojiFilter.filterEmoji(old_user.getNick_name()));
            old_user.setGender(old_user.getGender());
            old_user.setCity(old_user.getCity());
            old_user.setProvince(old_user.getProvince());
            old_user.setCountry(old_user.getCountry());
            old_user.setAvatar_url(old_user.getAvatar_url());
            this.appUserService.saveOrUpdate(old_user);
        } else {
            logger.info("新用户首次登录，保存");
            AppUserEntity new_user = new AppUserEntity();
            new_user.setId(IdWorker.getFlowIdWorkerInstance().nextId() + "");
            new_user.setUser_name(mobile);
            new_user.setMobile(mobile);

            new_user.setNick_name((EmojiFilter.filterEmoji(userInfoVo.getNickName()))==null?mobile:EmojiFilter.filterEmoji(userInfoVo.getNickName()));

            new_user.setGender(userInfoVo.getGender());
            new_user.setCity(userInfoVo.getCity());
            new_user.setProvince(userInfoVo.getProvince());
            new_user.setCountry(userInfoVo.getCountry());
            new_user.setAvatar_url(userInfoVo.getAvatarUrl());
            new_user.setOpenid(sessionVo.getOpenid());
            new_user.setSession_key(sessionVo.getSessionKey());
            new_user.setCreate_time(new Date());
            new_user.setLastvisit_time(new Date());
            new_user.setUser_type(0);
            new_user.setStatus(0);
            this.appUserService.saveOrUpdate(new_user);
        };
        //生成用户个人的token
        //生成token并添加到缓存
        String token = UUID.randomUUID().toString();
        logger.info("login token："+token);
        redisUtils.set(token, sessionVo.getOpenid()+ "&&" + sessionVo.getSessionKey(), Constant.TOKEN_EXPIRE);
        userInfoVo.setToken(token);
        userInfoVo.setCreateTime(new Date());
        userInfoVo.setMobile(ajaxResult.getData().toString());
        userInfoVo.setUserType(old_user.getUser_type());
        return AjaxResult.builder().code(200).msg("OK").result(userInfoVo).build();
    }

    public AjaxResult GetMobile(String encryptedData,String iv,String sessionKey) {
        try {
            // 被加密的数据
            byte[] dataByte = Base64.getDecoder().decode(encryptedData);
            // 偏移量
            byte[] ivByte = Base64.getDecoder().decode(iv);
            // 加密秘钥
            byte[] keyByte = Base64.getDecoder().decode(sessionKey);

            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                JSONObject jsonObject = JSONObject.fromObject(result);
                return AjaxResult.builder().code(200).msg("ok").result(jsonObject.get("phoneNumber")).build();
            }else{
                return AjaxResult.builder().code(202).msg("登录失败，手机号获取无效").build();
            }
        } catch (Exception e) {
            logger.error("login errot:" + e.getMessage());
            return AjaxResult.builder().code(202).msg(e.getMessage()).build();
        }
    }
   /*
       * 验证登录
       * @return
   */
   @GetMapping("/api/validateLogin")
   public AjaxResult validateToken(@CurrentUser UserBean user){
       if(user==null){
           return AjaxResult.builder().code(203).msg("token 验证失败，请重新登录").build();
       };
       return AjaxResult.builder().code(200).msg("token 验证成功").build();
   }
    }

