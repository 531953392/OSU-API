
package com.lvb.baseApi.restful.enroll.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lvb.baseApi.restful.enroll.dao.EnrollUserInfoMapper;
import com.lvb.baseApi.restful.enroll.entity.AppEnrollUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
@Transactional(rollbackFor = Exception.class)
public class EnrollUserInfoService extends ServiceImpl<EnrollUserInfoMapper, AppEnrollUser> {

    public List<AppEnrollUser> getByUserId(String userId) {
        return baseMapper.getByUserId(userId);
    }



}
