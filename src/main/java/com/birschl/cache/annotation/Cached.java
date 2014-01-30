package com.birschl.cache.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.birschl.cache.providers.CacheProvider;
import com.birschl.cache.providers.HashMapCacheProvider;



@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)

public @interface Cached {
	
	public enum CacheScope {CLASS, OBJECT};
	
	public Class<? extends CacheProvider> cacheProvider() default HashMapCacheProvider.class;

	public CacheScope cacheScope() default CacheScope.OBJECT;
	
	public long expirationTime() default 0;
}