package com.lvb.baseApi.restful.doctor.vo;

import com.lvb.baseApi.restful.doctor.entity.DoctorInfoEntity;
import com.lvb.baseApi.restful.order.entity.OrderEntity;
import com.lvb.baseApi.restful.user.entity.AppUserEntity;
import lombok.Data;

@Data
public class DoctorInfoVo {

    public DoctorInfoEntity doctorInfoEntity;

    public AppUserEntity appUserEntity;

    public OrderEntity orderEntity;

}
