
package com.lvb.baseApi.restful.enroll.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lvb.baseApi.restful.enroll.entity.AppEnroll;
import com.lvb.baseApi.restful.enroll.entity.AppEnrollList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


@Mapper
public interface EnrollListMapper extends BaseMapper<AppEnrollList> {

    List<AppEnrollList> getArticleList(IPage<AppEnrollList> page, @Param("map") Map<String,Object> map);

}
