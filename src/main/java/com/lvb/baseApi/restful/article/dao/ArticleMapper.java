
package com.lvb.baseApi.restful.article.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lvb.baseApi.restful.article.entity.AppArticle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


@Mapper
public interface ArticleMapper extends BaseMapper<AppArticle> {

    List<AppArticle> getArticleList(IPage<AppArticle> page, @Param("map") Map<String,Object> map);

}
