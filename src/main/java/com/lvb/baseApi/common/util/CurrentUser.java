package com.lvb.baseApi.common.util;

import java.lang.annotation.*;

/**
 * <p>绑定当前登录的用户</p>
 * <p>不同于@ModelAttribute</p>
 *
 * @author lism
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser {

    /**
     * 当前用户在request中的名字
     *
     * @return
     */
    String value() default "user";

}
