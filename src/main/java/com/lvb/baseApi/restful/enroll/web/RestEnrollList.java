package com.lvb.baseApi.restful.enroll.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lvb.baseApi.common.result.ResultPage;
import com.lvb.baseApi.common.result.ResultPageData;
import com.lvb.baseApi.restful.enroll.entity.AppEnrollList;
import com.lvb.baseApi.restful.enroll.service.EnrollListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value="/api/enrollList")
public class RestEnrollList {

    @Autowired
    private EnrollListService enrollService;


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResultPage getArticleList(Integer page, Integer pageSize) throws Exception {
        Map<String,Object> map = new HashMap<>();
        IPage<AppEnrollList> listPage = enrollService.getArticleList(new Page<>(page, pageSize),map);
        ResultPageData resultPageData = new ResultPageData(listPage.getCurrent(),listPage.getTotal(),listPage.getPages(),listPage.getRecords());
        ResultPage resultPage = new ResultPage(200, "返回列表",resultPageData);
        return resultPage;
    }



}
