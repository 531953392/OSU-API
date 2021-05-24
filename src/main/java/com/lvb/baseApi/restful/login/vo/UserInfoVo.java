package com.lvb.baseApi.restful.login.vo;

import lombok.Data;

import java.util.Date;

@Data
public class UserInfoVo {

    public String nickName;

    public int gender;

    public String city;

    public String province;

    public String country;

    public String avatarUrl;

    public String token;

    public Date createTime;

    private String mobile;

    private int userType;



}
