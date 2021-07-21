
package com.lvb.baseApi.restful.enroll.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lvb.baseApi.restful.enroll.entity.AppEnrollUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface EnrollUserInfoMapper extends BaseMapper<AppEnrollUser> {


    List<AppEnrollUser> getByUserId(@Param("userId") String userId);

}
