
package com.lvb.baseApi.restful.releaseInfo.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lvb.baseApi.restful.releaseInfo.dao.ReleaseInfoMapper;
import com.lvb.baseApi.restful.releaseInfo.entity.InfoGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


@Service
@Transactional(rollbackFor = Exception.class)
public class ReleaseInfoService extends ServiceImpl<ReleaseInfoMapper, InfoGroup> {



    public IPage<InfoGroup> getInfoGroupList(Page<InfoGroup> page, Map<String,Object> map) {
        return page.setRecords(baseMapper.getInfoGroupList(page,map));
    }

    public IPage<InfoGroup> getMyInfoGroupList(Page<InfoGroup> page, Map<String,Object> map) {
        return page.setRecords(baseMapper.getMyInfoGroupList(page,map));
    }

    public int waitAuditCount(String userid) {
        return baseMapper.waitAuditCount(userid);
    }

    public int auditFailCount(String userid) {
        return baseMapper.auditFailCount(userid);
    }
    public int totalCount(String userid) {
        return baseMapper.totalCount(userid);
    }

}
