
package com.lvb.baseApi.restful.shop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lvb.baseApi.restful.shop.entity.AppShop;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


@Mapper
public interface ShopMapper extends BaseMapper<AppShop> {

    List<AppShop> getShopList(IPage<AppShop> page, @Param("map") Map<String,Object> map);

}
