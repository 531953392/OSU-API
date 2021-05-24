package com.lvb.baseApi.common.entity;

import lombok.Data;

import java.util.Date;

/*
* 数据实体类
* */

@Data
public class DataEntity {


    /*
    * 创建者
    * */

    private String create_by;


    /**
     * GMT 创建时间
     */
    private Date create_time;


    /*
    * 更新者
    * */
    private String update_by;

    /**
     * GMT 修改时间
     */
    private Date update_time;




}
