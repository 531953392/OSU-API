
package com.lvb.baseApi.restful.article.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lvb.baseApi.restful.article.dao.ArticleMapper;
import com.lvb.baseApi.restful.article.entity.AppArticle;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


@Service
@Transactional(rollbackFor = Exception.class)
public class ArticleService extends ServiceImpl<ArticleMapper, AppArticle> {



    public IPage<AppArticle> getArticleList(Page<AppArticle> page, Map<String,Object> map) {
        return page.setRecords(baseMapper.getArticleList(page,map));
    }


}
