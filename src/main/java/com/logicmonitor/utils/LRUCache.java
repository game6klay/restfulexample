package com.logicmonitor.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jay on 8/12/17.
 */
public class LRUCache implements Cache {

	private static final int CAPACITY = 100000;
	private Map<String, Node> map;
	private Node head;
	private Node end;
	private static LRUCache lru;
	/**
	 * Cache as an entire structure is maintain in Nodes with pointer Head and End 
	 * The get and set are the primary contract 
	 * Map is the integral data structure used to store the equivalent Node objects 
	 * 
	 * */
	private LRUCache() {
		map = new HashMap<String, Node>();
		head = null;
		end = null;
	}
	
	public static LRUCache getInstance(){
		if(null == lru)
			lru = new LRUCache();
		return lru;
	}

	/**
	 * This method checks in the map and if present removes the node first 
	 * then sets the Node as the Head of the structure 
	 * */
	
	public String get(String shortURL) {
		if (map.containsKey(shortURL)) {
			System.out.println("in get "+shortURL);
			Node n = map.get(shortURL);
			remove(n);
			setHead(n);
			return n.longURL;
		}
		System.out.println("not in cache");
		throw new IllegalArgumentException("Need to search in the Database");
	}
	
	public int mapSize() {
		return map.size();
	} 

	public void remove(Node n) {
		if (n.pre != null) {
			n.pre.next = n.next;
		} else {
			head = n.next;
		}

		if (n.next != null) {
			n.next.pre = n.pre;
		} else {
			end = n.pre;
		}
	}

	public void setHead(Node n) {
		n.next = head;
		n.pre = null;

		if (head != null)
			head.pre = n;

		head = n;

		if (end == null)
			end = head;
	}

	/**
	 * If the node is already present then set it as the head node after remove it from the structure
	 * If not present then add the Node and set it as head.
	 * */
	
	public void set(String shortURL, String longURL) {
		System.out.println("in set "+longURL);
		if (map.containsKey(shortURL)) {
			Node old = map.get(shortURL);
			old.longURL = longURL;
			remove(old);
			setHead(old);
		} else {
 			Node created = new Node(shortURL, longURL);
			if (map.size() >= CAPACITY) {
				map.remove(end.shortURL);
				remove(end);
				setHead(created);

			} else {
				setHead(created);
			}

			map.put(shortURL, created);
			System.out.println("Map size "+map.size());
		}
	}
}