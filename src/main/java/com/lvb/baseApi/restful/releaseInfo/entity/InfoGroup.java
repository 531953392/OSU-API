package com.lvb.baseApi.restful.releaseInfo.entity;

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
@TableName(value = "app_info_group")
@Data
public class InfoGroup implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String id;

    private String user_id;

    private Integer info_type;

    private String info_content;

    private String image_url_1;

    private String image_url_2;

    private String image_url_3;

    private String image_url_4;

    private String user_name;

    private String user_nick_name;

    private String user_avatar_url;


    /** 状态（0正常 1关闭） */
    private Integer status;

    /** 创建者 */
    private String create_by;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date create_time;

    /** 更新者 */
    private String update_by;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date update_time;

    /** 备注 */
    private String remark;

}
