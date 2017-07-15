package com.yun.audiobook.children;

import java.util.List;

public class Context {
	private static Context instance = null;
	private List<String> bookFictionList;
	private List<String> bookNoFictionList;

	private Context() {

	}

	public static Context getInstance() {
		if (instance == null) {
			instance = new Context();
		}
		return instance;
	}

	public List<String> getBookFictionList() {
		return bookFictionList;
	}

	public void setBookFictionList(List<String> bookFictionList) {
		this.bookFictionList = bookFictionList;
	}

	public List<String> getBookNoFictionList() {
		return bookNoFictionList;
	}

	public void setBookNoFictionList(List<String> bookNoFictionList) {
		this.bookNoFictionList = bookNoFictionList;
	}

}
