package com.lvb.baseApi.restful.releaseInfo.web;

import com.aliyun.oss.OSSClient;
import com.lvb.baseApi.aliyunOss.AliyunOSSClientUtil;
import com.lvb.baseApi.common.result.AjaxResult;
import com.lvb.baseApi.common.util.CurrentUser;
import com.lvb.baseApi.common.util.IdWorker;
import com.lvb.baseApi.common.util.UserBean;
import com.lvb.baseApi.restful.releaseInfo.entity.InfoGroup;
import com.lvb.baseApi.restful.releaseInfo.service.ReleaseInfoService;
import com.lvb.baseApi.restful.user.entity.AppUserEntity;
import com.lvb.baseApi.restful.user.service.AppUserService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;


@RestController
@RequestMapping(value="/api/releaseInfo")
public class RestInfoGroup {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private ReleaseInfoService releaseInfoService;


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

}
