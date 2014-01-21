package com.birschl.cache;

import java.util.HashMap;
import java.util.Map;

import com.birschl.cache.annotation.Cached;
import com.birschl.cache.providers.HashMapCacheProvider;


public class CacheTestClass {

	private Map<String, String> testData = new HashMap<String, String>();

	public CacheTestClass() {}

	CacheTestClass(Map<String, String> testData) {
		this.testData = testData;
	}

	Map<String, String> getDataMap() {
		return testData;
	}

	void setTestData(Map<String, String> testData) {
		this.testData = testData;
	}

	@Cached(cacheProvider = HashMapCacheProvider.class)
	String getValue(String key) {
		return testData.get(key);
	}

}