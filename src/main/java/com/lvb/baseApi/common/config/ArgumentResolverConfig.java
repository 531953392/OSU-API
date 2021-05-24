package com.lvb.baseApi.common.config;

import com.lvb.baseApi.common.util.CurrentUserMethodArgumentResolver;
import com.lvb.baseApi.common.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;


//获取openid
@Configuration
public class ArgumentResolverConfig extends WebMvcConfigurationSupport {

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        System.out.println("注册CurrentUserMethodArgumentResolver");
        argumentResolvers.add(new CurrentUserMethodArgumentResolver(redisUtils));
    }



}


