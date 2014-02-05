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

import java.util.HashMap;
import java.util.Map;

import com.birschl.cache.annotation.Cached;
import com.birschl.cache.annotation.Cached.CacheScope;
import com.birschl.cache.providers.PersistentCacheProvider;

public class TestModelObjectScope {

	private Map<String, String> testData = new HashMap<String, String>();

	public TestModelObjectScope() {}

	TestModelObjectScope(Map<String, String> testData) {
		this.testData = testData;
	}

	Map<String, String> getDataMap() {
		return testData;
	}

	void setTestData(Map<String, String> testData) {
		this.testData = testData;
	}

	@Cached(cacheScope = CacheScope.OBJECT)
	String getValue(String key) {
		return testData.get(key);
	}

	@Cached(cacheProvider = PersistentCacheProvider.class, cacheScope = CacheScope.OBJECT)
	String getValuePersistent(String key) {
		return testData.get(key);
	}

}