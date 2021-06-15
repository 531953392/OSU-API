
package com.lvb.baseApi.restful.releaseInfo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lvb.baseApi.restful.releaseInfo.entity.InfoGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


@Mapper
public interface ReleaseInfoMapper extends BaseMapper<InfoGroup> {

    List<InfoGroup> getInfoGroupList(IPage<InfoGroup> page, @Param("map") Map<String,Object> map);

    List<InfoGroup> getMyInfoGroupList(IPage<InfoGroup> page, @Param("map") Map<String,Object> map);



    @Select("SELECT count(*) FROM app_info_group a where a.user_id= #{userid}")
    int totalCount(@Param("userid")String userid);

    @Select("SELECT count(*) FROM app_info_group a where a.user_id= #{userid} and a.audit_type=10")
    int waitAuditCount(@Param("userid")String userid);

    @Select("SELECT count(*) FROM app_info_group a where a.user_id= #{userid} and a.audit_type=30")
    int auditFailCount(@Param("userid")String userid);
}
