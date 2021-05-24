package com.lvb.baseApi.restful.doctor.web;

import com.lvb.baseApi.common.result.AjaxResult;
import com.lvb.baseApi.common.util.CurrentUser;
import com.lvb.baseApi.common.util.UserBean;
import com.lvb.baseApi.restful.doctor.entity.DoctorInfoEntity;
import com.lvb.baseApi.restful.doctor.service.DoctorService;
import com.lvb.baseApi.restful.doctor.vo.UserDoctorInfo;
import com.lvb.baseApi.restful.user.entity.AppUserEntity;
import com.lvb.baseApi.restful.user.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 邓小顺
 */
@RestController
@RequestMapping(value="/doctorInfo")
public class RestDoctor {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AppUserService appUserService;
    //医生信息
    @RequestMapping("/getDoctorInfo")
    public AjaxResult getDoctorInfo(@CurrentUser UserBean user) {

        AppUserEntity appUserEntity = appUserService.getUserById(user.getOpenid());
        if(appUserEntity==null){
            return AjaxResult.builder().code(203).msg("用户信息不存在!").build();
        };
        UserDoctorInfo userDoctorInfo = new UserDoctorInfo();
        DoctorInfoEntity doctorInfoEntity = doctorService.getUserId(appUserEntity.getId());
        userDoctorInfo.setAppUserEntity(appUserEntity);
        userDoctorInfo.setDoctorInfoEntity(doctorInfoEntity);
        return AjaxResult.builder().code(200).msg("OK").result(userDoctorInfo).build();
    }

    //自动匹配机制
    @RequestMapping("/getAutoDoctorInfo")
    public AjaxResult getAutoDoctorInfo() {
//        if(user ==null ){
//            return AjaxResult.builder().code(203).msg("用户信息不存在!").build();
//        };
        DoctorInfoEntity doctorInfoEntity = doctorService.getAutoDoctorInfo();
        return AjaxResult.builder().code(200).msg("OK").result(doctorInfoEntity).build();
    }

}
