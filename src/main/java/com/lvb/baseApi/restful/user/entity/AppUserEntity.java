package com.lvb.baseApi.restful.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "app_user")
public class AppUserEntity   implements Serializable {

    private String user_name;

    private String id;

    private String openid;

    private String nick_name;

    private String avatar_url;

    private Integer gender;

    private String country;

    private String province;

    private String city;

    private String mobile;

    private Date create_time;

    private String session_key;

    private Date lastvisit_time;

    private Integer order_number;

    private Integer user_type;

    private Integer status;

}
