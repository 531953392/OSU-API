package com.lvb.baseApi.restful.doctor.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 *  医生信息
 */
@Data
@TableName(value = "app_doctor_info")
public class DoctorInfoEntity  implements Serializable {

    public String id;

    public String user_name;

    public Integer order_number;

    public Integer status;

    public String user_id;

    public String full_name;

    public String label;

    public String number;

    public String remarks;

    public String head_portrait;

    public Integer sort;

    public String openid;


}
