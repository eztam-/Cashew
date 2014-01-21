package com.birschl.cache;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CacheTest {

	private Map<String, String> testData = new HashMap<String, String>();

	@Before
	public void init() {
		testData.put("Key A", "Value A");
		testData.put("Key B", "Value B");
		testData.put("Key C", "Value C");
	}

	@Test
	public void testCache() throws MCacheException {
		CacheTestClass cachedObject = MCache.newCachedInstance(CacheTestClass.class);

		assertEquals(null, cachedObject.getValue("Key A"));
		cachedObject.setTestData(testData);
		assertEquals(null, cachedObject.getValue("Key A"));
		assertEquals("Value B", cachedObject.getValue("Key B"));

		CacheTestClass cachedObject2 = MCache.newCachedInstance(CacheTestClass.class);
		cachedObject2.setTestData(testData);

		assertEquals("Value A", cachedObject2.getValue("Key A"));
		assertEquals("Value B", cachedObject2.getValue("Key B"));
		assertEquals("Value C", cachedObject2.getValue("Key C"));
		cachedObject2.getDataMap().clear();
		assertEquals("Value A", cachedObject2.getValue("Key A"));
		assertEquals("Value B", cachedObject2.getValue("Key B"));
		assertEquals("Value C", cachedObject2.getValue("Key C"));
	}


	@Test
	public void testCacheInstantiation() throws MCacheException {
		
		Class<?>[] types = {Map.class};
		Object[] params = { testData };

		CacheTestClass cachedObject = MCache.newCachedInstance(CacheTestClass.class, types, params);

		assertEquals("Value A", cachedObject.getValue("Key A"));
		assertEquals("Value B", cachedObject.getValue("Key B"));
		assertEquals("Value C", cachedObject.getValue("Key C"));
		cachedObject.getDataMap().clear();
		assertEquals("Value A", cachedObject.getValue("Key A"));
		assertEquals("Value B", cachedObject.getValue("Key B"));
		assertEquals("Value C", cachedObject.getValue("Key C"));
	}

}
