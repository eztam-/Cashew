package com.birschl.cache;

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
			InvocationTargetException, InstantiationException {

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

	private CacheProvider getCacheProvider(Cached cachedAnnotation, Method method) throws InstantiationException, IllegalAccessException {

		Class<? extends CacheProvider> cacheProviderType = cachedAnnotation.cacheProvider();
		String methodId = getUniqueMethodId(method);

		CacheProvider cacheProvider;
		boolean isObjectScoped = cachedAnnotation.cacheScope() == CacheScope.OBJECT;
		Map<String, CacheProvider> cacheProviders = isObjectScoped ? objectCacheProviders : classCacheProviders;

		cacheProvider = cacheProviders.get(methodId);
		if (cacheProvider == null)
		{
			cacheProvider = cacheProviderType.newInstance();
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
