package com.yun.audiobook.children.database;

public class History {
	public static final int TYPE_FAV = 1;
	public static final int TYPE_READ = 2;
	private long id;
	private int type;
	private String book;
	private String chapter;
	private String time;
	private String link;
	private int stopPoint;
	private String imgUrl;
	private int chapterIndex;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getChapter() {
		return chapter;
	}

	public void setChapter(String chapter) {
		this.chapter = chapter;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getBook() {
		return book;
	}

	public void setBook(String book) {
		this.book = book;
	}

	public int getStopPoint() {
		return stopPoint;
	}

	public void setStopPoint(int stopPoint) {
		this.stopPoint = stopPoint;
	}

	@Override
	public String toString() {
		return "History [id=" + id + ", type=" + type + ", book=" + book + ", chapter=" + chapter + ", time=" + time
				+ ", link=" + link + "]";
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public int getChapterIndex() {
		return chapterIndex;
	}

	public void setChapterIndex(int chapterIndex) {
		this.chapterIndex = chapterIndex;
	}

}
