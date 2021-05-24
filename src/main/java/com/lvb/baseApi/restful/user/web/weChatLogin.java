//package com.lvb.baseApi.restful.user.web;
//
//import com.alibaba.fastjson.JSON;
//import com.lvb.baseApi.common.result.AjaxResult;
//import com.lvb.baseApi.restful.user.entity.AppUserEntity;
//import com.lvb.baseApi.restful.user.service.AppUserService;
//import com.lvb.baseApi.restful.user.vo.WeChatLoginModel;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import java.util.Date;
//import java.util.UUID;
//
//public class weChatLogin {
//
//    @Autowired
//    private AppUserService appUserService;
//
//    /**
//     * 微信小程序登录
//     * 登录成功后，将用户身份信息及session_key存入token
//     * @param model
//     * @return
//     */
//    @ResponseBody
//    @PostMapping("/weChatLogin")
//    public AjaxResult weChatLogin(@RequestBody WeChatLoginModel model){
//
//        //第三步：调用service.weChatLogin(model):后台检查openid是否存在，返回openid对应的用户
//        WeChatLoginResult<UserAccount> loginResult = service.weChatLogin(model);
//
//        //第四步：
//        UserAccount user = loginResult.getUser();
//        if(user == null ){
//            result.setCode(0);
//            result.setMessage("登录失败");
//        }
//        else {
//            User u = new User();
//            u.setId(user.getId());
//            u.setPassword(user.getPassword() == null ? user.getWxopenid() : user.getPassword());
//            u.setSessionKey(loginResult.getSession_key());
//            String token = getToken(u);
//            result.setToken(token);
//            result.setCode(1);
//            result.setMessage("登陆成功");
//        }
//
//        return AjaxResult.builder().code(200).msg("OK").result(brandShopEntityList).build();;
//    }
//
//
//    public AjaxResult weChatLogin(WeChatLoginModel model){
//        AppUserEntity result = null;
//        try {
//
//            // code  -> openid
//            String urlFormat = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";
//            String url = String.format(urlFormat, WeChat.appId, WeChat.secret, model.getCode());
//
//
//            if(result.getErrcode() == null){
//                // 去数据库 检查 openId 是否存在 不存在就新建用户
//                AppUserEntity user =appUserService.getUserById(result.getOpenid());
//                if(user == null || user.getId() == null){
//                    // 不存在，就是第一次登录：新建用户信息
//                    AppUserEntity userInfo = new AppUserEntity();
//                    userInfo.setOpenid(result.getOpenid());
//                    userInfo.setCreate_time(new Date());
//                    this.appUserService.saveOrUpdate(userInfo);
//                } else {
//                    //如果存在，就不是第一次登录，更新最后登录时间
//                    user.setLastvisit_time(new Date());
//                    this.appUserService.saveOrUpdate(user);
//                }
//                result.setUser(user);
//
//                // 保存登录日志
//               /* LoginLog log = new LoginLog();
//                log.setId(UUID.randomUUID().toString());
//                log.setCreatetime(new Date());
//                log.setLogindate(new Date());
//                log.setUserid(user.getId());
//                log.setWxcode(model.getCode());
//                loginLog.insert(log);*/
//            }
//            else {
//                System.out.println(json);
//            }
//        }
//        catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//        return result;
//    }
//
//}
