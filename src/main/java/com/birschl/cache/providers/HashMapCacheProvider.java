package com.birschl.cache.providers;

import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.MultiKeyMap;

public class HashMapCacheProvider implements CacheProvider {

	private MultiKeyMap<Object, Object> cache = new MultiKeyMap<Object, Object>();

	private long expirationTimeS = 0;
	private long creationTimeMs = 0;

	public Object getCachedResult(Object[] keys) {
		checkExpiration();
		checkNumArguments(keys);
		return cache.get(new MultiKey<Object>(keys));
	}

	public void put(Object[] keys, Object value) {
		checkExpiration();
		checkNumArguments(keys);
		cache.put(new MultiKey<Object>(keys), value);
	}

	public boolean contains(Object[] keys) {
		checkExpiration();
		checkNumArguments(keys);
		return cache.containsKey(new MultiKey<Object>(keys));
	}

	protected void checkNumArguments(Object[] keys) {
		if (keys.length > 5)
		{
			throw new HashMapCacheProviderException(
					"The CacheProvider "
							+ getClass().getCanonicalName()
							+ " is just applicable to methods with a maximum amount of five parameters");
		}
	}

	private void checkExpiration() {
		if (expirationTimeS == 0)
			return;
		else if (creationTimeMs == 0)
		{
			creationTimeMs = System.currentTimeMillis();
			return;
		}
		double lifeTimeMs = (System.currentTimeMillis() - creationTimeMs);
		if (lifeTimeMs > expirationTimeS * 1000)
		{
			cache.clear();
			creationTimeMs = System.currentTimeMillis();
		}

	}

	public void setExpirationTime(long expirationTime) {
		this.expirationTimeS = expirationTime;
	}

	public class HashMapCacheProviderException extends RuntimeException {

		private static final long serialVersionUID = -516314348256116583L;

		HashMapCacheProviderException(String message) {
			super(message);
		}
	}
}
