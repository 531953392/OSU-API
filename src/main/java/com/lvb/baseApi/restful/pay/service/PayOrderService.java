
package com.lvb.baseApi.restful.pay.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lvb.baseApi.restful.pay.dao.PayOrderMapper;
import com.lvb.baseApi.restful.pay.entity.PayOrderEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(rollbackFor = Exception.class)
public class PayOrderService extends ServiceImpl<PayOrderMapper, PayOrderEntity>{

    public PayOrderEntity getByOrderId(String orderId) {
        return baseMapper.getByOrderId(orderId);
    }

    public PayOrderEntity getByOrderId(String orderId,Integer payType) {
        return baseMapper.getByOrderId(orderId,payType);
    }

    public PayOrderEntity getByMerOrderId(String merOrderId) {
        return baseMapper.getByMerOrderId(merOrderId);
    }
}
