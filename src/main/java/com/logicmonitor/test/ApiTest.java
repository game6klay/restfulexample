package com.logicmonitor.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.logicmonitor.dao.DocumentDaoImpl;

public class ApiTest {

	static HttpPost postRequest;
	static DefaultHttpClient httpClient;
	static HttpResponse response;
	static DocumentDaoImpl daoImpl;
	static Gson gson;

	@BeforeClass
	public static void classSetUp() throws Exception {
		daoImpl = new DocumentDaoImpl();
		gson = new Gson();
		httpClient = new DefaultHttpClient();
		postRequest = new HttpPost("http://localhost:8080/RESTfulExample/rest/urlService/RESTfulExample");
	}

	@AfterClass
	public static void classTearDown() throws Exception {
		httpClient.getConnectionManager().shutdown();
	}

	@After
	public void tearDown() throws IOException {
	}

	@BeforeClass
	public static void createNewShortenedUrlWhenUrlDoesNotExist() {

		String url = "http://www.google.com";
		StringEntity input;
		try {
			input = new StringEntity("{\"url\":\"http://www.google.com\",\"shorten\":\"true\",\"blacklist\":false}");
			input.setContentType("application/json");
			postRequest.setEntity(input);
			response = httpClient.execute(postRequest);
			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			String json = br.readLine();
			JsonParser parser = new JsonParser();
			JsonObject obj = (parser.parse(json).getAsJsonObject());
			String shortURL = obj.get("shortURL").getAsString();
			assertEquals(daoImpl.findShortURL(url), shortURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void shouldReturnExistingUrlForAlreadyPostedURL() {
		String shortUrl = daoImpl.findShortURL("http://www.google.com");
		StringEntity input;
		try {
			input = new StringEntity("{\"url\":\"http://www.google.com\",\"shorten\":\"true\",\"blacklist\":\"false\"}");
			input.setContentType("application/json");
			postRequest.setEntity(input);
			response = httpClient.execute(postRequest);
			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			String json = br.readLine();
			System.out.println(json);
			JsonParser parser = new JsonParser();
			JsonObject obj = (parser.parse(json).getAsJsonObject());
			String shortURL = obj.get("shortURL").getAsString();
			assertEquals(shortUrl, shortURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void shouldReturnOriginalUrlIfGivenValidShortUrl() {
		String url = "http://www.google.com";
		String shortURL = daoImpl.findShortURL(url);
		StringEntity input;
		try {
			input = new StringEntity("{\"url\":\""+shortURL+"\",\"shorten\":\"false\",\"blacklist\":\"false\"}");
			input.setContentType("application/json");
			postRequest.setEntity(input);
			response = httpClient.execute(postRequest);
			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			String json = br.readLine();
			JsonParser parser = new JsonParser();
			JsonObject obj = (parser.parse(json).getAsJsonObject());
			String longURL = obj.get("longURL").getAsString();
			assertEquals(url, longURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void shouldReturnProjectUrlWhenRequestedUrlDoesnotExist() {
		String url = "http://localhost:8080/RESTfulExample/";
		StringEntity input;
		try {
			input = new StringEntity("{\"url\":\"http://localhost:8080/RESTfulExample/rd/00sadf\",\"shorten\":\"false\",\"blacklist\":\"false\"}");
			input.setContentType("application/json");
			postRequest.setEntity(input);
			response = httpClient.execute(postRequest);
			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			String json = br.readLine();
			JsonParser parser = new JsonParser();
			JsonObject obj = (parser.parse(json).getAsJsonObject());
			String longURL = obj.get("longURL").getAsString();
			assertEquals(url, longURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
