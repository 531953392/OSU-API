
package com.lvb.baseApi.restful.doctor.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lvb.baseApi.restful.doctor.dao.DoctorMapper;
import com.lvb.baseApi.restful.doctor.entity.DoctorInfoEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class DoctorService extends ServiceImpl<DoctorMapper, DoctorInfoEntity>{

    public DoctorInfoEntity getAutoDoctorInfo() {
        return baseMapper.getAutoDoctorInfo();
    }

    public DoctorInfoEntity getUserId(String userid) {
        return baseMapper.getUserId(userid);
    }

}
