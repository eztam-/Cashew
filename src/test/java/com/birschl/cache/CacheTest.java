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
	public void testObjectScopeCache() throws MCacheException {
		TestModelObjectScope cachedObject = MCache.newCachedInstance(TestModelObjectScope.class);

		assertEquals(null, cachedObject.getValue("Key A"));
		cachedObject.setTestData(testData);
		assertEquals("Value B", cachedObject.getValue("Key B"));
		cachedObject.getDataMap().put("Key A", "Value A");
		assertEquals(null, cachedObject.getValue("Key A"));

		TestModelObjectScope cachedObject2 = MCache.newCachedInstance(TestModelObjectScope.class);
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
	public void testClassScopeCache() throws MCacheException {
		TestModelClassScope cachedObject = MCache.newCachedInstance(TestModelClassScope.class);

		assertEquals(null, cachedObject.getValue("Key A"));
		cachedObject.setTestData(testData);
		assertEquals(null, cachedObject.getValue("Key A"));
		assertEquals("Value B", cachedObject.getValue("Key B"));
		cachedObject.getDataMap().put("Key A", "Value A");
		assertEquals(null, cachedObject.getValue("Key A"));

		TestModelClassScope cachedObject2 = MCache.newCachedInstance(TestModelClassScope.class);

		assertEquals(null, cachedObject2.getValue("Key A"));
		assertEquals("Value B", cachedObject2.getValue("Key B"));
		assertEquals(null, cachedObject2.getValue("Key C"));
		cachedObject2.getDataMap().put("Key C", "Value C");
		assertEquals(null, cachedObject2.getValue("Key C"));
	}

	@Test
	public void testCacheInstantiation() throws MCacheException {
		
		Class<?>[] types = {Map.class};
		Object[] params = { testData };

		TestModelObjectScope cachedObject = MCache.newCachedInstance(TestModelObjectScope.class, types, params);

		assertEquals("Value A", cachedObject.getValue("Key A"));
		assertEquals("Value B", cachedObject.getValue("Key B"));
		assertEquals("Value C", cachedObject.getValue("Key C"));
		cachedObject.getDataMap().clear();
		assertEquals("Value A", cachedObject.getValue("Key A"));
		assertEquals("Value B", cachedObject.getValue("Key B"));
		assertEquals("Value C", cachedObject.getValue("Key C"));
	}

}
