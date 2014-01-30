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

import java.lang.reflect.Method;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.ProxyObject;

public class ProxyFactory {
	public static <T> T newCachedInstance(Class<T> clazz) throws CachewException
	{
		try
		{
			javassist.util.proxy.ProxyFactory factory = new javassist.util.proxy.ProxyFactory();
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
			throw new CachewException(e);
		}
		throw new CachewException("Object instantiation failed"); // This will never happen
	}

	public static <T> T newCachedInstance(Class<T> clazz, Class<?>[] constructorTypes, Object[] constructorArgs) throws CachewException {
		try
		{
			javassist.util.proxy.ProxyFactory factory = new javassist.util.proxy.ProxyFactory();
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
			throw new CachewException(e);
		}
		throw new CachewException("Object instantiation failed"); // This will never happen
	}

	public static <T> T newCachedInstance(Class<T> clazz, Class<?> constructorParamType,
			Object contructorParam) throws CachewException
	{
		Class<?>[] constructorTypes = { constructorParamType };
		Object[] constructorArgs = { contructorParam };
		return newCachedInstance(clazz, constructorTypes, constructorArgs);
		
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
