
package com.lvb.baseApi.restful.enroll.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lvb.baseApi.restful.enroll.dao.EnrollMapper;
import com.lvb.baseApi.restful.enroll.entity.AppEnroll;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


@Service
@Transactional(rollbackFor = Exception.class)
public class EnrollService extends ServiceImpl<EnrollMapper, AppEnroll> {



    public IPage<AppEnroll> getArticleList(Page<AppEnroll> page, Map<String,Object> map) {
        return page.setRecords(baseMapper.getArticleList(page,map));
    }


}
