package com.yun.android.provider.librivox;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.yun.android.provider.ParsedObject;

public class LVoxChapter extends ParsedObject {
	private String title;

	private String url = "";
	private long fileSize;
	private String duration;

	public LVoxChapter(XmlPullParser xpp) throws IOException, XmlPullParserException {
		int eventType = xpp.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {

			eventType = xpp.getEventType();
			if (eventType == XmlPullParser.START_TAG) {

				if (xpp.getName().equalsIgnoreCase("title")) {
					title = parseStringVal(xpp).trim();

				} else if (xpp.getName().equalsIgnoreCase("link")) {
					url = parseStringVal(xpp).trim();
				} else if (xpp.getName().equalsIgnoreCase("enclosure")) {
					if (url.equals("")) {
						url = xpp.getAttributeValue(null, "url");
					}

					try {
						String lengthString = xpp.getAttributeValue(null, "length");
						if (lengthString.endsWith("MB")) {
							lengthString.replace("MB", "");
							fileSize = (long) (Float.valueOf(lengthString) * 1000000);
						} else
							fileSize = Long.valueOf(lengthString);
					} catch (NumberFormatException e) {
						fileSize = 0;
					}
				}else if (xpp.getName().equalsIgnoreCase("duration")) {
					duration = parseStringVal(xpp).trim();
				}
			}
			if (eventType == XmlPullParser.END_TAG) {
				if (xpp.getName().equalsIgnoreCase("item")) {
					break;
				}
			}
			// if(eventType == XmlPullParser.TEXT) {
			// //System.out.println("Text "+xpp.getText());
			// }
			eventType = xpp.next();
		}
	}

	public String getTitle() {
		return title;
	}

	public String getUrl() {
		return url;
	}

	public long getFileSize() {
		return fileSize;
	}

	public String getDuration() {
		return duration;
	}

}
