package com.logicmonitor.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.logicmonitor.dao.DocumentDaoImpl;

public class Base62 {
	private static Logger log=Logger.getLogger(Base62.class);
	private long counter;
	private Map<Long, String> indexToUrl;
	private Map<String, Long> urlToIndex;
	private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	DocumentDaoImpl db = new DocumentDaoImpl();
	
	/**
	 * Singleton Pattern implemented to make sure the only one instance is initiated and thus the 
	 * counter is not initiated with 1 every time the value is calculated 
	 * 
	 * */
	
	private static Base62 singleBase62Instance;
	
	private Base62 () {
		counter = decode(db.getCounter())+1L;
		System.out.println("counter: "+counter );
		indexToUrl = new HashMap<Long, String>();
		urlToIndex = new HashMap<String, Long>();
	}
	
	public static Base62 getInstance(){
		if(null == singleBase62Instance)
			singleBase62Instance = new Base62();
		return singleBase62Instance;
	}

	// Encodes a URL to a shortened URL.
	public String encode(String longUrl) {
	    if (urlToIndex.containsKey(longUrl)) {
	        return base62Encode(urlToIndex.get(longUrl));
	    }
	    else {
	        indexToUrl.put(counter, longUrl);
	        urlToIndex.put(longUrl, counter);
	        counter++;
	        return base62Encode(urlToIndex.get(longUrl));
	    }
	}

	// Decodes a shortened URL to its original URL.
	public long decode(String shortUrl) {
		long decode = 0;
		try{
			String base62Encoded = shortUrl.substring(shortUrl.lastIndexOf("/") + 1);
		    for(int i = 0; i < base62Encoded.length(); i++) {
		        decode = decode * 62 + BASE62.indexOf("" + base62Encoded.charAt(i));
		    }
		}catch(Exception ex)
		{
			log.error("Error: "+ex.getMessage());
		}
	    
	    return decode;
	}

	private String base62Encode(long value) {
		StringBuilder sb = new StringBuilder();
		try{
			while (value != 0) {
		        sb.append(BASE62.charAt((int)(value % 62)));
		        value /= 62;
		    }
		    while (sb.length() < 6) {
		        sb.append(0);
		    }
		}catch(Exception ex)
		{
			log.error("Error: "+ex.getMessage());
		}
	    
	    
	    return sb.reverse().toString();
	}

	public Long getCounter() {
		return counter;
	}

	public void setCounter(Long counter) {
		this.counter = counter;
	}
	
	
}
