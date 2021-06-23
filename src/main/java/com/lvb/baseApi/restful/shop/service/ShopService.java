
package com.lvb.baseApi.restful.shop.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lvb.baseApi.restful.shop.dao.ShopMapper;
import com.lvb.baseApi.restful.shop.entity.AppShop;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


@Service
@Transactional(rollbackFor = Exception.class)
public class ShopService extends ServiceImpl<ShopMapper, AppShop> {



    public IPage<AppShop> getShopList(Page<AppShop> page, Map<String,Object> map) {
        return page.setRecords(baseMapper.getShopList(page,map));
    }


}
