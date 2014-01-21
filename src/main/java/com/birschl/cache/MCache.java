package com.birschl.cache;

import java.lang.reflect.Method;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

public class MCache {
	public static <T> T newCachedInstance(Class<T> clazz) throws MCacheException
	{
		try
		{
			ProxyFactory factory = new ProxyFactory();
			factory.setSuperclass(clazz);
			factory.setFilter(new MethodFilter() {
				public boolean isHandled(Method m) {
					// ignore finalize()
					return !m.getName().equals("finalize");
				}
			});

			Class<?> c = factory.createClass();
			Object obj = c.newInstance();
			if (clazz.isInstance(obj))
			{
				T proxy = clazz.cast(obj);
				((ProxyObject) proxy).setHandler(new CachedMethodHandler());
				return proxy;
			}

		} catch (Exception e)
		{
			throw new MCacheException(e);
		}
		throw new MCacheException("Object instantiation failed"); // This will never happen
	}

	public static <T> T newCachedInstance(Class<T> clazz, Class<?>[] constructorTypes, Object[] constructorArgs) throws MCacheException {
		try
		{
			ProxyFactory factory = new ProxyFactory();
			factory.setSuperclass(clazz);
			factory.setFilter(new MethodFilter() {
				public boolean isHandled(Method m) {
					// ignore finalize()
					return !m.getName().equals("finalize");
				}
			});
			Object o = factory.create(constructorTypes, constructorArgs, new CachedMethodHandler());
			if (clazz.isInstance(o))
				return clazz.cast(o);

		} catch (Exception e)
		{
			throw new MCacheException(e);
		}
		throw new MCacheException("Object instantiation failed"); // This will never happen
	}
	//
	// public static void attachToCache(Object o) {
	//
	//
	// }
	//
	// public static void detachFromCache() {
	// }

}
