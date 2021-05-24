package com.lvb.baseApi.common.util;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.annotation.Resource;

/**
 * 用于绑定@CurrentUser的方法参数解析器
 *
 * @author lism
 */
public class CurrentUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    //@Resource
   private RedisUtils redisUtils;



    public CurrentUserMethodArgumentResolver(RedisUtils redisUtils ) {
        this.redisUtils = redisUtils;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.getParameterType().isAssignableFrom(UserBean.class) && parameter.hasParameterAnnotation(CurrentUser.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        CurrentUser currentUserAnnotation = parameter.getParameterAnnotation(CurrentUser.class);
        //从Session 获取用户
        Object object = webRequest.getAttribute(currentUserAnnotation.value(), NativeWebRequest.SCOPE_SESSION);
        //从  accessToken获得用户信息
        if (object == null) {
            String token = webRequest.getHeader("token");
            String[] obj= redisUtils.get(token)==null?null:redisUtils.get(token).split("&&");
            if(obj==null||obj.length==0){
                return object;
            };
            System.out.println("openid:"+obj[0]);
            return new UserBean(obj[0]);
        }
        return object;
    }
}
