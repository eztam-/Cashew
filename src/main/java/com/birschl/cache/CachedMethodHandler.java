package com.birschl.cache;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javassist.util.proxy.MethodHandler;

import com.birschl.cache.annotation.Cached;
import com.birschl.cache.providers.CacheProvider;

public class CachedMethodHandler implements MethodHandler {

	private Map<String, CacheProvider> cacheProviders = new HashMap<String, CacheProvider>();

	public Object invoke(Object self, Method m, Method proceed, Object[] args)
			throws Throwable {

		Annotation annotation = m.getAnnotation(Cached.class);
		if (annotation != null)
		{
			Cached cachedAnnotation = (Cached) annotation;
			Class<? extends CacheProvider> cacheProviderType = cachedAnnotation
					.cacheProvider();

			String methodId = getUniqueMethodId(m);
			CacheProvider cacheProvider = cacheProviders.get(methodId);
			if (cacheProvider == null)
			{
				cacheProvider = cacheProviderType.newInstance();
				cacheProviders.put(methodId, cacheProvider);
			}

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
