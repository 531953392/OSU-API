package com.lvb.baseApi.restful.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lvb.baseApi.common.entity.DataEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *  订单
 */
@Data
@TableName(value = "app_order")
public class OrderEntity  implements Serializable {

    public String id;

    public String doctor_id;

    public String user_id;

    public String name;

    //状态- 10:待支付，20:等待回复，30:已完成，40：已取消，50：已过期
    public int status;

    public BigDecimal amount;

    public Date create_time;

    public String remark;

    public int interrogation_type;

    public int pet_type;

    public boolean isTwopay;

    public BigDecimal two_pay_amount;

    public int talk_time;

    public boolean isPay;

    public Date pay_time;


    public Date reply_time;

    public String weChatNo;

    public String price_type;



}
