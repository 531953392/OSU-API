package com.lvb.baseApi.restful.order.vo;

import com.lvb.baseApi.restful.doctor.entity.DoctorInfoEntity;
import com.lvb.baseApi.restful.order.entity.OrderEntity;
import com.lvb.baseApi.restful.user.entity.AppUserEntity;
import lombok.Data;

import java.util.List;

@Data
public class OrderInfoVo {

    public DoctorInfoEntity doctorInfoEntity;

    public AppUserEntity appUserEntity;

    public OrderEntity orderEntity;

    public AppUserEntity appDoctorUserEntity;


}
