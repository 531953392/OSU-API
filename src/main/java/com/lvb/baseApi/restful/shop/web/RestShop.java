package com.lvb.baseApi.restful.shop.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lvb.baseApi.common.result.ResultPage;
import com.lvb.baseApi.common.result.ResultPageData;
import com.lvb.baseApi.restful.shop.entity.AppShop;
import com.lvb.baseApi.restful.shop.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value="/api/shop")
public class RestShop {

    @Autowired
    private ShopService shopService;


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResultPage getShopList(Integer page, Integer pageSize, Integer type,String searchKey) throws Exception {
        Map<String,Object> map = new HashMap<>();
        map.put("type",type==-1?null:type);
        map.put("searchKey",searchKey==""?null:searchKey);
        IPage<AppShop> listPage = shopService.getShopList(new Page<>(page, pageSize),map);
        ResultPageData resultPageData = new ResultPageData(listPage.getCurrent(),listPage.getTotal(),listPage.getPages(),listPage.getRecords());
        ResultPage resultPage = new ResultPage(200, "返回列表",resultPageData);
        return resultPage;
    }



}
