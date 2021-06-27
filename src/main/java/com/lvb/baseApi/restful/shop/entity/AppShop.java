package com.lvb.baseApi.restful.shop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 【请填写功能名称】对象 app_shop
 * 
 * @author dxs
 * @date 2021-06-23
 */
@TableName(value = "app_shop")
@Data
public class AppShop implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 商家ID */
    private String id;

    /** 商家名字 */
    private String name;

    /** 商家LOGO */
    private String logo;

    /** 商家类型 */
    private Long type;

    /** 商家地址 */
    private String address;

    /** 商家介绍 */
    private String introduction;

    /** 营业时间 */
    private String hours;

    /** 电话 */
    private String telephone;

    /** 状态 */
    private String status;

    /** 排序 */
    private Long sort;

    /** 商家图片1 */
    private String image1;

    /** 商家图片2 */
    private String image2;

    /** 商家图片3 */
    private String image3;

    /** 商家图片4 */
    private String image4;

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

    private String menu_url;

    private String menu_url2;



}
