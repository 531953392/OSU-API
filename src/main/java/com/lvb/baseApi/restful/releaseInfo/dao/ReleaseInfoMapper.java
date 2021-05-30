
package com.lvb.baseApi.restful.releaseInfo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lvb.baseApi.restful.article.entity.AppArticle;
import com.lvb.baseApi.restful.releaseInfo.entity.InfoGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


@Mapper
public interface ReleaseInfoMapper extends BaseMapper<InfoGroup> {

    List<InfoGroup> getInfoGroupList(IPage<InfoGroup> page, @Param("map") Map<String,Object> map);

}
