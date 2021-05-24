package com.lvb.baseApi.restful.user.vo;

import lombok.Data;

@Data
public class UserInfoVo {

    private String token;

    private String nick_name;

    private String avatar_url;

    private Integer gender;

    private String country;

    private String province;

    private String city;

    private String language;



}
