package com.cxqm.xiaoerke.modules.sys.interceptor;

import java.lang.annotation.*;

/**
 *Controller
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemControllerLog {
    String description() default "";
}
