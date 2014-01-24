package com.birschl.cache;

import java.util.HashMap;
import java.util.Map;

import com.birschl.cache.annotation.Cached;
import com.birschl.cache.annotation.Cached.CacheScope;

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

}