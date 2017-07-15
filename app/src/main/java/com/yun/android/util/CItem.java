package com.yun.android.util;

public class CItem {

	private String id = "";
	private String value = "";

	public CItem() {
		id = "";
		value = "";
	}

	public CItem(String _id, String _value) {
		id = _id;
		value = _value;
	}

	@Override
	public String toString() { 
		return value;
	}

	public String getId() {
		return id;
	}

	public String getValue() {
		return value;
	}

}