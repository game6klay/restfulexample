package com.logicmonitor.test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.logicmonitor.utils.Base62;

public class UrlConversionTest {

	static Base62 base62;
	@BeforeClass
	public static void setUp(){
		base62 = Base62.getInstance();
	}

	@Test
	public void shoulReturnValidEncoding() {
		String url = "http://www.google.com";
		String output = "000008";
		base62.setCounter(8L);
		assertEquals(output, base62.encode(url));
	}
	
	@Test
	public void shouldGiveOriginalCounterValueOnDecode()
	{
		System.out.println(base62.decode("000008"));
		assertEquals(8, base62.decode("000008"));
	}
}