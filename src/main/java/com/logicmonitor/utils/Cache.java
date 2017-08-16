package com.logicmonitor.utils;

public interface Cache {

	String get (String shortUrl);
	
	void set (String shortUrl, String longUrl); 
}
