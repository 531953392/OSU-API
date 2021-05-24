
package com.lvb.baseApi.restful.user.service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lvb.baseApi.restful.user.dao.AppUserMapper;
import com.lvb.baseApi.restful.user.entity.AppUserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 品牌店铺实现类
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AppUserService extends ServiceImpl<AppUserMapper, AppUserEntity>{



    public AppUserEntity getUserById(String openid) {
        return baseMapper.getUserById(openid);
    }

    public AppUserEntity getUserId(String userId) {
        return baseMapper.getUserId(userId);
    }


}
