package com.lvb.baseApi.restful.article.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

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
    private String articleId;

    /** 文章标题 */
    private String articleTitle;

    /** 文章类型 */
    private Long articleTyle;

    /** 文章摘要 */
    private String articleSummary;

    /** 文章详情链接 */
    private String articleUrl;

    /** 文章LOGO */
    private String articleLogo;

    /** 状态（0正常 1关闭） */
    private String status;

}
