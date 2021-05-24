
package com.lvb.baseApi.restful.order.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lvb.baseApi.restful.order.dao.OrderMapper;
import com.lvb.baseApi.restful.order.entity.OrderEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;



@Service
@Transactional(rollbackFor = Exception.class)
public class OrderService extends ServiceImpl<OrderMapper, OrderEntity>{



    public IPage<OrderEntity> getDoctorOrderList(Page<OrderEntity> page,Map<String,Object> map) {
        return page.setRecords(baseMapper.getDoctorOrderList(page,map));
    }

    public IPage<OrderEntity> getOrderList(Page<OrderEntity> page,Map<String,Object> map) {
        return page.setRecords(baseMapper.getOrderList(page,map));
    }

    public OrderEntity getByUserId(String userid) {
        return baseMapper.getByUserId(userid);
    }

    public OrderEntity getByUserName(String userName) {
        return baseMapper.getByUserName(userName);
    }

    public int getWaitPayCount(String userid) {
        return baseMapper.getWaitPayCount(userid);
    }

    public int getWaitReplyCount(String userid) {
        return baseMapper.getWaitReplyCount(userid);
    }
    public int getWaitRepairCount(String userid) {
        return baseMapper.getWaitRepairCount(userid);
    }

}
