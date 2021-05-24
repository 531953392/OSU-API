
package com.lvb.baseApi.restful.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lvb.baseApi.restful.user.entity.AppUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 品牌店铺
 */
@Mapper
public interface AppUserMapper extends BaseMapper<AppUserEntity> {

    @Select("SELECT * FROM app_user  a where a.openid= #{openid}")
    AppUserEntity getUserById(@Param("openid")String openid);

    @Select("SELECT * FROM app_user  a where a.id= #{userId}")
    AppUserEntity getUserId(@Param("userId")String userId);

}
