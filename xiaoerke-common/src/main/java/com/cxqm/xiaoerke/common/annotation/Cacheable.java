package com.cxqm.xiaoerke.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Cacheable {
	
	String key() default "";

	/**
	 * the unit is hour
	 * @return
	 */
	int expire() default 0;

	String condition() default "";
	
	String unless() default "";
}
