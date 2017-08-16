package com.logicmonitor.manager;

import java.net.UnknownHostException;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class DbConnectionManager {
	private MongoClient mongo;
	private DB db;
	private DBCollection collection;
	
	/**
	 * Mongodb connection handler which acquires the connection till the server restarts  
	 * */
	
	public DbConnectionManager() {
		try {
			mongo = new MongoClient("localhost", 27017);
			db = mongo.getDB("logicmonitor");
			collection = db.getCollection("ShortToLong");
		} catch (UnknownHostException e) {
		}
	}
	
	public DBCollection getCollection() {
		return collection;
	}

	public void setCollection(DBCollection collection) {
		this.collection = collection;
	}
}