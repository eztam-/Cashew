/* Copyright (C) 2014 by Matthias Birschl (m-birschl@gmx.de)
 * 
 * This file is part of Cashew.
 * Cashew is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 * 
 * Cashew is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

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