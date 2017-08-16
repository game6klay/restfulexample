package com.logicmonitor.dao;
/*
 * @author: jay
 * This interface lists the method to be implemented by DocumentDaoImpl
 */
import java.util.List;

import com.logicmonitor.model.Document;

public interface DocumentDao {
	boolean insertDocument(String shortURL, String longURL);
	String findLongURL(String shortURL);
	String getCounter();
	List<Document> getAllURLs();
	String findShortURL(String longURL);
	void updateBlacklist(String shortURL);
}
