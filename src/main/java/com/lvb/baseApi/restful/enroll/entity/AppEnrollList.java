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
@TableName(value = "app_article")
@Data
public class AppEnrollList implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 题目ID */
    private String id;

    /** 题目标题 */
    private String title;

    /** 排序 */
    private String sort;

    /** 状态 */
    private String status;

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


}
