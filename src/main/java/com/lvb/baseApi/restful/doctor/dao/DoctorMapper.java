
package com.lvb.baseApi.restful.doctor.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lvb.baseApi.restful.doctor.entity.DoctorInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


/**
 * 品牌店铺
 */
@Mapper
public interface DoctorMapper extends BaseMapper<DoctorInfoEntity> {

    @Select("select * from app_doctor_info a where a.Id>0 and a.status=0 order by a.order_number asc  limit 0,1")
    DoctorInfoEntity getAutoDoctorInfo();


    @Select("select * from app_doctor_info a where a.user_id=#{userid} and a.status=0 order by a.id asc limit 0,1")
    DoctorInfoEntity getUserId(@Param("userid") String userid);



}
