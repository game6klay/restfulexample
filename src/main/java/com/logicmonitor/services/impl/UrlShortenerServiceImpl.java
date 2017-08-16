package com.logicmonitor.services.impl;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.logicmonitor.constants.Constants;
import com.logicmonitor.dao.DocumentDaoImpl;
import com.logicmonitor.model.Document;
import com.logicmonitor.services.urlShortnerService;
import com.logicmonitor.utils.Base62;
import com.logicmonitor.utils.LRUCache;

/**
 * Rest API of the structures which processes the User request
 * */

@Path("/urlService")
public class UrlShortenerServiceImpl implements urlShortnerService {
	private static Logger log = Logger.getLogger(UrlShortenerServiceImpl.class);
	Base62 singleBase62Instance = Base62.getInstance();
	DocumentDaoImpl db = new DocumentDaoImpl();
	Gson gson = new Gson();

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/urlShortner")
	public String getTransformedURL(String data) {
		/**
		 * Parse Json and applies the operation according to the user request
		 * */
		JsonParser parser = new JsonParser();
		JsonObject obj = (parser.parse(data).getAsJsonObject());
		boolean shorten = obj.get("shorten").getAsBoolean();
		boolean blacklist = obj.get("blacklist").getAsBoolean();
		String url = obj.get("url").getAsString();
		Document document = new Document();
		if (shorten && !blacklist) {
			String shortUrl = singleBase62Instance.encode(url);
			shortUrl = Constants.URL_PREFIX + shortUrl;
			db.insertDocument(shortUrl, url);
			document.setShortURL(shortUrl);
			document.setLongURL(url);
			document.setBlacklist(false);
			log.info("service accessed " + 200);
			return gson.toJson(document);
		} else if(!shorten && !blacklist) {
			LRUCache lru = LRUCache.getInstance();
			String lengthenUrl = "";
			document.setShortURL(url);
			try {
				lengthenUrl = lru.get(url);
			} catch (IllegalArgumentException ex) {
				lengthenUrl = db.findLongURL(Constants.URL_PREFIX + url);
			}
			document.setLongURL(lengthenUrl);
			log.info("service accessed " + 200);
			return gson.toJson(document);
		}
		else{
			db.updateBlacklist(url);
			document.setShortURL(url);
			document.setBlacklist(true);
			return gson.toJson(document);
		}
	}
}