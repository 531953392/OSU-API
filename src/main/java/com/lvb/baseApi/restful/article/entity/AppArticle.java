package com.lvb.baseApi.restful.article.entity;

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
public class AppArticle implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 文章ID */
    private String article_id;

    /** 文章标题 */
    private String article_title;

    /** 文章类型 */
    private Long article_tyle;

    /** 文章摘要 */
    private String article_summary;

    /** 文章详情链接 */
    private String article_url;

    /** 文章LOGO */
    private String article_logo;

    /** 状态（0正常 1关闭） */
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

    /** 备注 */
    private String remark;

    /** 排序 */
    private Long sort;

}
