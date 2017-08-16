package com.logicmonitor.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.logicmonitor.utils.LRUCache;

public class LruCacheTest {

	static LRUCache cache;
	@Before 
	public void setUp() throws IOException {
		cache = LRUCache.getInstance();
	}
	
	@After 
	public void afterAll() throws IOException {
		
	}
	
	@Test
	public void test() {
		
		for (int i=1;i<100001;i++) {
			cache.set(""+i, ""+i);
		}
	    assertEquals(100000, cache.mapSize());
	    System.out.println(cache.mapSize());
	    cache.set("100001", "100001");
	    cache.set("100002", "100002");
	    
	    assertEquals(100000, cache.mapSize());
	    System.out.println(cache.mapSize());;
	    
	}

}
