package com.lvb.baseApi.restful.pay.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *  支付订单 状态
 */
@Data
@TableName(value = "app_pay_order")
public class PayOrderEntity implements Serializable {

    public String id;

    public String order_id;

    public String user_id;

    public Integer pay_type; //10:首次 20:补付

    public Date pay_time;

    public double amount;

    public String traceNo;

    public String body;

    public String merId;

    public String pay_result_code;


}
