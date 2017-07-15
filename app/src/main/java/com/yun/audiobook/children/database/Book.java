package com.yun.audiobook.children.database;

import java.io.Serializable;

public class Book implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO
	 */

	private static final long serialVersionUID = 1L;
	private long id;
	private String title;
	private String imgUrl;
	private String rssUrl;

	public Book(String title, String imgUrl, String rssUrl ) {
		this.title = title;
		this.imgUrl = imgUrl;
		this.rssUrl = rssUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getRssUrl() {
		return rssUrl;
	}

	public void setRssUrl(String rssUrl) {
		this.rssUrl = rssUrl;
	}

	@Override
	public String toString() {
		return title;
	}

}
