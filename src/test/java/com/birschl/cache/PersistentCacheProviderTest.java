package com.birschl.cache;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class PersistentCacheProviderTest {
	private Map<String, String> testData = new HashMap<String, String>();

	@Before
	public void init() {
		testData.put("Key A", "Value A");
		testData.put("Key B", "Value B");
		testData.put("Key C", "Value C");
	}

	@Test
	public void testPersistentCache() throws CachewException {
		TestModelClassScope cachedObject = ProxyFactory.newCachedInstance(TestModelClassScope.class);

		assertEquals(null, cachedObject.getValuePersistent("Key A"));
		cachedObject.setTestData(testData);
		assertEquals(null, cachedObject.getValuePersistent("Key A"));
		assertEquals("Value B", cachedObject.getValuePersistent("Key B"));
		cachedObject.getDataMap().put("Key A", "Value A");
		assertEquals(null, cachedObject.getValuePersistent("Key A"));

		TestModelClassScope cachedObject2 = ProxyFactory.newCachedInstance(TestModelClassScope.class);

		assertEquals(null, cachedObject2.getValuePersistent("Key A"));
		assertEquals("Value B", cachedObject2.getValuePersistent("Key B"));
		assertEquals(null, cachedObject2.getValuePersistent("Key C"));
		cachedObject2.getDataMap().put("Key C", "Value C");
		assertEquals(null, cachedObject2.getValuePersistent("Key C"));

	}
}
