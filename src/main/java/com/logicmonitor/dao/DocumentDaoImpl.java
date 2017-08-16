package com.logicmonitor.dao;

import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.Node;

import com.google.gson.Gson;
import com.logicmonitor.manager.DbConnectionManager;
import com.logicmonitor.model.Document;
import com.logicmonitor.utils.LRUCache;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
/**
 * This class contains implementation of the contract of the Database operations 
 * The operations are implemented with help of the Gson library to packaging of the JSON objects
 * 
 * */
public class DocumentDaoImpl implements DocumentDao {
	DbConnectionManager db = new DbConnectionManager();
	Gson gson = new Gson();

	/**
	 * The fields in the database are ID , shortURL , longURL and blacklist boolean 
	 * Due to the ID which is extracted by hashing function of Base62 essentially the 
	 * 6 last characters of the shortened URL. 
	 * */
	
	/**
	 * Fetches the values send from the API and inserts them into the database
	 * */
	
	public boolean insertDocument(String shortURL, String longURL) {
		BasicDBObject searchQuery = new BasicDBObject().append("shortURL", shortURL);
		DBObject removeIdProjection = new BasicDBObject("_id", 0);
		DBCursor cursor = db.getCollection().find(searchQuery,removeIdProjection);
		if(cursor.size() == 0){
			BasicDBObject document = new BasicDBObject();

			document.put("_id", shortURL.substring(shortURL.lastIndexOf("/")+1,shortURL.length()));
			document.put("shortURL", shortURL);
			document.put("longURL", longURL);
			document.put("blacklist", false);
			db.getCollection().insert(document);
			return true;
		}
		return false;
	}

	/**
	 * With the shortened URL it gets the ID and extracts the original URL from the database
	 * */
	public String findLongURL(String shortURL) {
		BasicDBObject searchQuery = new BasicDBObject().append("_id", shortURL.substring(shortURL.lastIndexOf("/")+1,shortURL.length()));
		DBCursor cursor = db.getCollection().find(searchQuery);
		if(cursor.hasNext()){
			String dbObject = cursor.next().toString();
			System.out.println(dbObject);
			Document doc = gson.fromJson(dbObject, Document.class);
			System.out.println("Bhavantest "+doc.isBlacklist());
			if(doc.isBlacklist())
				return "http://localhost:8080/RESTfulExample/";
			System.out.println("LongURL: "+doc.getLongURL());
			return doc.getLongURL();
		}
		else
			return "http://localhost:8080/RESTfulExample/";
	}
	
	/**
	 * Finds the shortURL from the database with the long URL. The complexity of this operation 
	 * will not be O(n). This method is for testing purposes thus does not affect the complexity of the
	 * implementation.
	 * */
	
	public String findShortURL(String longURL) {
		BasicDBObject searchQuery = new BasicDBObject().append("longURL", longURL);
		DBCursor cursor = db.getCollection().find(searchQuery);
		if(cursor.hasNext()){
			String dbObject = cursor.next().toString();
			Document doc = gson.fromJson(dbObject, Document.class);
			System.out.println("LongURL: "+doc.getLongURL());
			return doc.getShortURL();
		}
		else
			return "";
	}
	
	/**
	 * The purpose of this method is that if the server restarts then it counter is not initiated with 
	 * initial value again inspite of data being present in the database which may create collisions
	 * */

	public String getCounter(){
		BasicDBObject searchQuery = new BasicDBObject();
		DBCursor cursor = db.getCollection().find(searchQuery).sort(new BasicDBObject("_id",-1)).limit(1);
		String counter = "0";
		Document doc = new Document();
		if(cursor.hasNext()){
			doc = gson.fromJson(cursor.next().toString(), Document.class);
			counter = doc.get_id();
		}
		return counter;
	}
	
	/**
	 * To display all the URL processed on the main UI
	 * */

	public List<Document> getAllURLs(){
		System.out.println("in all urls");
		List<Document> docs = new ArrayList<Document>(); 
		BasicDBObject searchQuery = new BasicDBObject();
		DBCursor cursor = db.getCollection().find(searchQuery).sort(new BasicDBObject("_id",-1));
		while(cursor.hasNext()){
			docs.add(gson.fromJson(cursor.next().toString(), Document.class));
		}
		System.out.println("in all urls"+docs.size());
		return docs;
	}
	
	/**
	 * To implement the trigger of blacklist a particular URL processed 
	 * */
	
	public void updateBlacklist(String shortURL){
		BasicDBObject query = new BasicDBObject();
		query.put("_id", shortURL.substring(shortURL.lastIndexOf("/")+1,shortURL.length()));

		BasicDBObject newDocument = new BasicDBObject();
		newDocument.put("blacklist", true);

		BasicDBObject updateObj = new BasicDBObject();
		updateObj.put("$set", newDocument);
		
		db.getCollection().update(query, updateObj);
	}
}
