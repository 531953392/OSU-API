package com.lvb.baseApi.restful.enroll.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 【请填写功能名称】对象 app_article
 * 
 * @author dxs
 * @date 2021-05-23
 */
@TableName(value = "app_enroll_user")
@Data
public class AppEnrollUser implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String id;

    private String user_id;

    private String enroll_id;

    private String enroll_title;

    private String enroll_value;

    private String user_name;

    private String user_nick_name;

    private String user_avatar_url;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date create_time;


}
