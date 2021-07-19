
package com.lvb.baseApi.restful.enroll.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lvb.baseApi.restful.enroll.entity.AppEnroll;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


@Mapper
public interface EnrollMapper extends BaseMapper<AppEnroll> {

    List<AppEnroll> getArticleList(IPage<AppEnroll> page, @Param("map") Map<String,Object> map);

}
