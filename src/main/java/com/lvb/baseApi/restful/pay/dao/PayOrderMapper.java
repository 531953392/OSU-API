
package com.lvb.baseApi.restful.pay.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lvb.baseApi.restful.pay.entity.PayOrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PayOrderMapper extends BaseMapper<PayOrderEntity> {


    @Select("SELECT * FROM app_pay_order  a where a.order_id= #{orderId}")
    PayOrderEntity getByOrderId(@Param("orderId") String orderId);

    @Select("SELECT * FROM app_pay_order  a where a.order_id= #{orderId} and a.pay_type=#{payType}")
    PayOrderEntity getByOrderId(@Param("orderId") String orderId,@Param("payType") Integer payType);

    @Select("SELECT * FROM app_pay_order  a where a.traceNo= #{merOrderId}")
    PayOrderEntity getByMerOrderId(@Param("merOrderId") String merOrderId);

}
