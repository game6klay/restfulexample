package com.logicmonitor.utils;

public class Node {
	String shortURL;
	String longURL;
	Node pre;
	Node next;

	public Node(String s, String l) {
		this.shortURL = s;
		this.longURL = l;
	}
}
