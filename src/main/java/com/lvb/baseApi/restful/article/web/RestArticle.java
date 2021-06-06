package com.lvb.baseApi.restful.article.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lvb.baseApi.common.result.ResultPage;
import com.lvb.baseApi.common.result.ResultPageData;
import com.lvb.baseApi.common.util.CurrentUser;
import com.lvb.baseApi.common.util.UserBean;
import com.lvb.baseApi.restful.article.entity.AppArticle;
import com.lvb.baseApi.restful.article.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value="/api/article")
public class RestArticle {

    @Autowired
    private ArticleService articleService;


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResultPage getArticleList(Integer page, Integer pageSize, Integer articleTyle,String searchKey) throws Exception {
        Map<String,Object> map = new HashMap<>();
        map.put("articleTyle",articleTyle==-1?null:articleTyle);
        map.put("searchKey",searchKey==""?null:searchKey);
        IPage<AppArticle> listPage = articleService.getArticleList(new Page<>(page, pageSize),map);
        ResultPageData resultPageData = new ResultPageData(listPage.getCurrent(),listPage.getTotal(),listPage.getPages(),listPage.getRecords());
        ResultPage resultPage = new ResultPage(200, "返回列表",resultPageData);
        return resultPage;
    }



}
