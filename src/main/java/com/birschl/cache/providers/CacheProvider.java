package com.birschl.cache.providers;

public interface CacheProvider {

	Object getCachedResult(Object[] keys);

	void put(Object[] keys, Object value);

	boolean contains(Object[] args);
	
}
