package com.lvb.baseApi.restful.doctor.vo;

import com.lvb.baseApi.restful.doctor.entity.DoctorInfoEntity;
import com.lvb.baseApi.restful.user.entity.AppUserEntity;
import lombok.Data;

@Data

public class UserDoctorInfo {

    public DoctorInfoEntity doctorInfoEntity;

    public AppUserEntity appUserEntity;
}
