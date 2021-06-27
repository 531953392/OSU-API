package com.lvb.baseApi.restful.user.web;


import com.lvb.baseApi.common.result.AjaxResult;
import com.lvb.baseApi.common.util.CurrentUser;
import com.lvb.baseApi.common.util.UserBean;
import com.lvb.baseApi.restful.user.entity.AppUserEntity;
import com.lvb.baseApi.restful.user.service.AppUserService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping(value="/api/user")
public class RestUser {

    @Autowired
    private AppUserService appUserService;

    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public AjaxResult details(@CurrentUser UserBean user)  {
        if(user ==null ){
            return AjaxResult.builder().code(203).msg("用户信息不存在!").build();
        };
        AppUserEntity appUserEntity = appUserService.getUserById(user.getOpenid());
        if(appUserEntity == null){
            return AjaxResult.builder().code(203).msg("用户信息不存在!").build();
        };
        return AjaxResult.builder().result(appUserEntity).code(200).msg("提交会员卡成功!").build();
    }

    @RequestMapping(value = "/postCardInfo", method = RequestMethod.POST)
    public AjaxResult postCardInfo(@RequestBody Map<String,Object> info, @CurrentUser UserBean user)  {
        if(user ==null ){
            return AjaxResult.builder().code(203).msg("用户信息不存在!").build();
        };
        AppUserEntity appUserEntity = appUserService.getUserById(user.getOpenid());
        if(appUserEntity == null){
            return AjaxResult.builder().code(203).msg("用户信息不存在!").build();
        };
        JSONObject model = JSONObject.fromObject( info.get("info"));
        String mobile = model.getString("mobile");
        String fullName = model.getString("fullName");
        String date = model.getString("date");
        appUserEntity.setFull_name(fullName);
        appUserEntity.setBirth_day(date);
        appUserEntity.setMobile(mobile);
        appUserEntity.setVip_card_status(1);
        appUserEntity.setApply_card_time(new Date());
        this.appUserService.saveOrUpdate(appUserEntity);
        return AjaxResult.builder().code(200).msg("提交会员卡成功!").build();
    }
}
