package com.cxqm.xiaoerke.common.annotation.interceptor;

import java.util.concurrent.TimeUnit;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.cxqm.xiaoerke.common.annotation.Cacheable;

//@Component (value = "simpleCacheInterceptor")
public class SimpleCacheInterceptor implements MethodInterceptor {

	@Autowired  
	private RedisTemplate<String, Object> redisTemplate;
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Cacheable cacheable = invocation.getMethod().getAnnotation(Cacheable.class);
		Object[] arguments = invocation.getArguments();
		String key = cacheable.key();
		
		if(key.indexOf("#{1}") != -1)
			key.replaceAll("#{1}", arguments[0] + "");
		if(key.indexOf("#{2}") != -1)
			key.replaceAll("#{2}", arguments[1] + "");
		if(key.indexOf("#{3}") != -1)
			key.replaceAll("#{3}", arguments[2] + "");
		if(key.indexOf("#{4}") != -1)
			key.replaceAll("#{4}", arguments[3] + "");
		if(key.indexOf("#{5}") != -1)
			key.replaceAll("#{5}", arguments[4] + "");
		
		int expire = cacheable.expire();
		Object obj = redisTemplate.opsForValue().get(key);
		if(obj != null)
			return obj;
		else {
			Object result = invocation.proceed();
			if(expire > 0)
				redisTemplate.opsForValue().set(key, result, expire, TimeUnit.HOURS);
			else
				redisTemplate.opsForValue().set(key, result);
			
			return result;
		}
	}
}
