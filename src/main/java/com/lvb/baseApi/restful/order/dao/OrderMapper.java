
package com.lvb.baseApi.restful.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lvb.baseApi.restful.order.entity.OrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


/**
 * 品牌店铺
 */
@Mapper
public interface OrderMapper extends BaseMapper<OrderEntity> {

    List<OrderEntity> getOrderList(IPage<OrderEntity> page, @Param("map") Map<String,Object> map);

    List<OrderEntity> getDoctorOrderList(IPage<OrderEntity> page, @Param("map") Map<String,Object> map);

    @Select("SELECT * FROM app_order a where a.user_id= #{userid} order by a.create_time desc limit 1")
    OrderEntity getByUserId(@Param("userid")String userid);

    @Select("SELECT * FROM app_order a where a.name= #{userName} and a.interrogation_type=20 and a.status=20 order by a.create_time desc limit 1")
    OrderEntity getByUserName(@Param("userName")String userName);

    @Select("SELECT count(*) FROM app_order a where a.user_id= #{userid} and a.status=10")
    int getWaitPayCount(@Param("userid")String userid);

    @Select("SELECT count(*) FROM app_order a where a.user_id= #{userid} and a.status=20")
    int getWaitReplyCount(@Param("userid")String userid);

    @Select("SELECT count(*) FROM app_order a where a.user_id= #{userid} and a.status>10 and a.isTwopay=true")
    int getWaitRepairCount(@Param("userid")String userid);

}
