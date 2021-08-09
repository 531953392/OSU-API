package com.lvb.baseApi.restful.enroll.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lvb.baseApi.common.result.AjaxResult;
import com.lvb.baseApi.common.result.ResultPage;
import com.lvb.baseApi.common.result.ResultPageData;
import com.lvb.baseApi.common.util.CurrentUser;
import com.lvb.baseApi.common.util.IdWorker;
import com.lvb.baseApi.common.util.UserBean;
import com.lvb.baseApi.restful.enroll.entity.AppEnroll;
import com.lvb.baseApi.restful.enroll.entity.AppEnrollUser;
import com.lvb.baseApi.restful.enroll.service.EnrollService;
import com.lvb.baseApi.restful.enroll.service.EnrollUserInfoService;
import com.lvb.baseApi.restful.user.entity.AppUserEntity;
import com.lvb.baseApi.restful.user.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value="/api/enroll")
public class RestEnroll {

    @Autowired
    private EnrollService enrollService;
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private EnrollUserInfoService EnrollUserInfoService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResultPage getArticleList(Integer page, Integer pageSize,String id) throws Exception {
        Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        IPage<AppEnroll> listPage = enrollService.getArticleList(new Page<>(page, pageSize),map);
        ResultPageData resultPageData = new ResultPageData(listPage.getCurrent(),listPage.getTotal(),listPage.getPages(),listPage.getRecords());
        ResultPage resultPage = new ResultPage(200, "返回列表",resultPageData);
        return resultPage;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public AjaxResult save(@RequestBody List<AppEnroll> info, @CurrentUser UserBean user)  {
        if(user ==null ){
            return AjaxResult.builder().code(203).msg("用户信息不存在!").build();
        };
        AppUserEntity appUserEntity = appUserService.getUserById(user.getOpenid());
        if(appUserEntity == null){
            return AjaxResult.builder().code(203).msg("用户信息不存在!").build();
        };
        if(info.size()!=0){
            List<AppEnrollUser> appEnrollUserList = EnrollUserInfoService.getByEnrollId(info.get(0).getEnroll_id());
            if(appEnrollUserList.size()!=0){
                return AjaxResult.builder().code(203).msg("你已提交过本期报名，请勿重复提交!").build();
            }else {
                for (AppEnroll appEnroll : info) {
                    AppEnrollUser item = new AppEnrollUser();
                    item.setId(IdWorker.getFlowIdWorkerInstance().nextId() + "");
                    item.setUser_id(appUserEntity.getId());
                    item.setUser_name(appUserEntity.getUser_name());
                    item.setUser_nick_name(appUserEntity.getNick_name());
                    item.setUser_avatar_url(appUserEntity.getAvatar_url());
                    item.setCreate_time(new Date());
                    item.setEnroll_id(appEnroll.getEnroll_id());
                    item.setEnroll_title(appEnroll.getProblem());
                    item.setEnroll_value(appEnroll.getValue());
                    EnrollUserInfoService.save(item);
                };
                return AjaxResult.builder().code(200).msg("提交报名成功!").build();
            }
        }else {
            return AjaxResult.builder().code(203).msg("提交报名失败!").build();
        }


    }

}
