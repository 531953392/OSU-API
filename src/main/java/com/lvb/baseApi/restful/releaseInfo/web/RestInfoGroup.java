package com.lvb.baseApi.restful.releaseInfo.web;

import com.aliyun.oss.OSSClient;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lvb.baseApi.aliyunOss.AliyunOSSClientUtil;
import com.lvb.baseApi.common.result.AjaxResult;
import com.lvb.baseApi.common.result.ResultPage;
import com.lvb.baseApi.common.result.ResultPageData;
import com.lvb.baseApi.common.util.CurrentUser;
import com.lvb.baseApi.common.util.IdWorker;
import com.lvb.baseApi.common.util.UserBean;
import com.lvb.baseApi.restful.article.entity.AppArticle;
import com.lvb.baseApi.restful.releaseInfo.entity.InfoGroup;
import com.lvb.baseApi.restful.releaseInfo.service.ReleaseInfoService;
import com.lvb.baseApi.restful.user.entity.AppUserEntity;
import com.lvb.baseApi.restful.user.service.AppUserService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value="/api/releaseInfo")
public class RestInfoGroup {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private ReleaseInfoService releaseInfoService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResultPage getArticleList(Integer page, Integer pageSize, Integer infoType, @CurrentUser UserBean user) throws Exception {
        Map<String,Object> map = new HashMap<>();
        if(user==null){
            return new ResultPage(203, "请先登录");
        };
        map.put("infoType",infoType==-1?null:infoType);
        IPage<InfoGroup> listPage = releaseInfoService.getInfoGroupList(new Page<>(page, pageSize),map);
        ResultPageData resultPageData = new ResultPageData(listPage.getCurrent(),listPage.getTotal(),listPage.getPages(),listPage.getRecords());
        ResultPage resultPage = new ResultPage(200, "返回列表",resultPageData);
        return resultPage;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public AjaxResult upload(@RequestBody Map<String,Object> info, @CurrentUser UserBean user)  {
        if(user ==null ){
            return AjaxResult.builder().code(203).msg("用户信息不存在!").build();
        };
        AppUserEntity appUserEntity = appUserService.getUserById(user.getOpenid());
        if(appUserEntity == null){
            return AjaxResult.builder().code(203).msg("用户信息不存在!").build();
        };
        JSONObject model = JSONObject.fromObject( info.get("info"));
        String pickerIndex = model.getString("pickerIndex");
        String textareaValue = model.getString("textareaValue");
        String  files = model.getString("files").replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\"","");
        String[] filesArr = files.split(",");
        InfoGroup infoGroup = new InfoGroup();
        infoGroup.setId(IdWorker.getFlowIdWorkerInstance().nextId() + "");
        infoGroup.setUser_id(appUserEntity.getId());
        infoGroup.setUser_name(appUserEntity.getUser_name());
        infoGroup.setUser_nick_name(appUserEntity.getNick_name());
        infoGroup.setUser_avatar_url(appUserEntity.getAvatar_url());
        infoGroup.setCreate_time(new Date());
        infoGroup.setStatus(0);
        infoGroup.setAudit_type(10);
        infoGroup.setCreate_by(appUserEntity.getUser_name());
        infoGroup.setInfo_type(Integer.parseInt(pickerIndex));
        infoGroup.setInfo_content(textareaValue);
        switch (filesArr.length){
            case 1:
                infoGroup.setImage_url_1(filesArr[0]);
                break;
            case 2:
                infoGroup.setImage_url_1(filesArr[0]);
                infoGroup.setImage_url_2(filesArr[1]);
                break;
            case 3:
                infoGroup.setImage_url_1(filesArr[0]);
                infoGroup.setImage_url_2(filesArr[1]);
                infoGroup.setImage_url_3(filesArr[2]);
                break;
            case 4:
                infoGroup.setImage_url_1(filesArr[0]);
                infoGroup.setImage_url_2(filesArr[1]);
                infoGroup.setImage_url_3(filesArr[2]);
                infoGroup.setImage_url_4(filesArr[3]);
                break;
        }
        this.releaseInfoService.saveOrUpdate(infoGroup);
        return AjaxResult.builder().code(200).msg("提交情报站成功!").build();
    }

    //状态统计
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public AjaxResult getReleaseInfoCount(@CurrentUser UserBean user){
        Map<String,Object> map = new HashMap<>();
        if(user ==null ){
            return AjaxResult.builder().code(203).msg("用户信息不存在!").build();
        };
        AppUserEntity appUserEntity = appUserService.getUserById(user.getOpenid());
        if(appUserEntity==null){
            return AjaxResult.builder().code(203).msg("用户信息不存在!").build();
        };
        int waitAuditCount =  releaseInfoService.waitAuditCount(appUserEntity.getId());
        int auditFailCount =  releaseInfoService.auditFailCount(appUserEntity.getId());
        int totalCount =  releaseInfoService.totalCount(appUserEntity.getId());
        map.put("totalCount",totalCount);
        map.put("waitAuditCount",waitAuditCount);
        map.put("auditFailCount",auditFailCount);
        return AjaxResult.builder().result(map).code(200).build();
    }

}
