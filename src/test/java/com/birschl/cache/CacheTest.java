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
	public void testObjectScopeCache() throws CachewException {
		TestModelObjectScope cachedObject = ProxyFactory.newCachedInstance(TestModelObjectScope.class);

		assertEquals(null, cachedObject.getValue("Key A"));
		cachedObject.setTestData(testData);
		assertEquals("Value B", cachedObject.getValue("Key B"));
		cachedObject.getDataMap().put("Key A", "Value A");
		assertEquals(null, cachedObject.getValue("Key A"));

		TestModelObjectScope cachedObject2 = ProxyFactory.newCachedInstance(TestModelObjectScope.class);
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
	public void testClassScopeCache() throws CachewException {
		TestModelClassScope cachedObject = ProxyFactory.newCachedInstance(TestModelClassScope.class);

		assertEquals(null, cachedObject.getValue("Key A"));
		cachedObject.setTestData(testData);
		assertEquals(null, cachedObject.getValue("Key A"));
		assertEquals("Value B", cachedObject.getValue("Key B"));
		cachedObject.getDataMap().put("Key A", "Value A");
		assertEquals(null, cachedObject.getValue("Key A"));

		TestModelClassScope cachedObject2 = ProxyFactory.newCachedInstance(TestModelClassScope.class);

		assertEquals(null, cachedObject2.getValue("Key A"));
		assertEquals("Value B", cachedObject2.getValue("Key B"));
		assertEquals(null, cachedObject2.getValue("Key C"));
		cachedObject2.getDataMap().put("Key C", "Value C");
		assertEquals(null, cachedObject2.getValue("Key C"));
	}

	@Test
	public void testCacheInstantiation() throws CachewException {

		Class<?>[] types = { Map.class };
		Object[] params = { testData };

		TestModelObjectScope cachedObject = ProxyFactory.newCachedInstance(TestModelObjectScope.class, types, params);

		assertEquals("Value A", cachedObject.getValue("Key A"));
		assertEquals("Value B", cachedObject.getValue("Key B"));
		assertEquals("Value C", cachedObject.getValue("Key C"));
		cachedObject.getDataMap().clear();
		assertEquals("Value A", cachedObject.getValue("Key A"));
		assertEquals("Value B", cachedObject.getValue("Key B"));
		assertEquals("Value C", cachedObject.getValue("Key C"));
	}

	@Test
	public void testCacheExpiration() throws CachewException, InterruptedException {
		TestModelClassScope cachedObject = ProxyFactory.newCachedInstance(TestModelClassScope.class);

		cachedObject.setTestData(testData);
		assertEquals("Value A", cachedObject.getValueExpiration("Key A"));
		cachedObject.getDataMap().clear();
		assertEquals("Value A", cachedObject.getValueExpiration("Key A"));
		Thread.sleep(1100);
		assertEquals(null, cachedObject.getValueExpiration("Key A"));

	}
}
