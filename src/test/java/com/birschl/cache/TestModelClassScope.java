package com.birschl.cache;

import java.util.HashMap;
import java.util.Map;

import com.birschl.cache.annotation.Cached;
import com.birschl.cache.annotation.Cached.CacheScope;

public class TestModelClassScope {
	private Map<String, String> testData = new HashMap<String, String>();

	public TestModelClassScope() {}

	TestModelClassScope(Map<String, String> testData) {
		this.testData = testData;
	}

	Map<String, String> getDataMap() {
		return testData;
	}

	void setTestData(Map<String, String> testData) {
		this.testData = testData;
	}

	@Cached(cacheScope = CacheScope.CLASS)
	String getValue(String key) {
		return testData.get(key);
	}

	@Cached(cacheScope = CacheScope.CLASS, expirationTime = 1)
	String getValueExpiration(String key) {
		return testData.get(key);
	}
}
