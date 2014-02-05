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

package com.birschl.cache;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javassist.util.proxy.MethodHandler;

import com.birschl.cache.annotation.Cached;
import com.birschl.cache.annotation.Cached.CacheScope;
import com.birschl.cache.providers.CacheProvider;

public class CachedMethodHandler implements MethodHandler {

	private Map<String, CacheProvider> objectCacheProviders = new HashMap<String, CacheProvider>();

	private static Map<String, CacheProvider> classCacheProviders = new HashMap<String, CacheProvider>();

	public Object invoke(Object self, Method m, Method proceed, Object[] args) throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, InstantiationException, SecurityException, NoSuchMethodException {

		Cached cachedAnnotation = m.getAnnotation(Cached.class);
		if (cachedAnnotation != null)
		{
			CacheProvider cacheProvider = getCacheProvider(cachedAnnotation, m);

			if (cacheProvider.contains(args))
			{
				return cacheProvider.getCachedResult(args);
			} else
			{
				Object result = proceed.invoke(self, args); // execute the original method.
				cacheProvider.put(args, result);
				return result;
			}
		}
		return proceed.invoke(self, args); // execute the original method.
	}

	private CacheProvider getCacheProvider(Cached cachedAnnotation, Method method) throws InstantiationException, IllegalAccessException,
			SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {

		Class<? extends CacheProvider> cacheProviderType = cachedAnnotation.cacheProvider();
		String methodId = getUniqueMethodId(method);

		CacheProvider cacheProvider;
		boolean isObjectScoped = cachedAnnotation.cacheScope() == CacheScope.OBJECT;
		Map<String, CacheProvider> cacheProviders = isObjectScoped ? objectCacheProviders : classCacheProviders;

		cacheProvider = cacheProviders.get(methodId);
		if (cacheProvider == null)
		{
			Constructor<? extends CacheProvider> constructor = cacheProviderType.getDeclaredConstructor(String.class);
			cacheProvider = (CacheProvider) constructor.newInstance(methodId);
			cacheProvider.setExpirationTime(cachedAnnotation.expirationTime());
			cacheProviders.put(methodId, cacheProvider);
		}

		return cacheProvider;
	}

	private String getUniqueMethodId(Method method) {
		String uniqueMethodId = method.getDeclaringClass().getCanonicalName()
				+ "." + method.getName();
		for (Class<?> paramType : method.getParameterTypes())
		{
			uniqueMethodId += "; " + paramType.getCanonicalName();
		}
		return uniqueMethodId;
	}

}
