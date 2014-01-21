package com.birschl.cache.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.birschl.cache.providers.CacheProvider;



@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)

public @interface Cached {
	public Class<? extends CacheProvider> cacheProvider();

}