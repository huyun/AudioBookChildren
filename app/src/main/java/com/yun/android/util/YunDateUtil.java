package com.yun.android.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Huyun
 * @version 1.0
 * @since 2012-02-22
 */
public class YunDateUtil {

	public static String formatDate(Date date, String pattern) {
		if (date == null)
			return "";
		if (pattern == null)
			pattern = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return (sdf.format(date));
	}

	public static String formatDateTime(Date date) {
		return (formatDate(date, "yyyy-MM-dd HH:mm:ss"));
	}

	public static String formatDateTime(Date date, String pattern) {
		return (formatDate(date, pattern));
	}

	public static String formatDateTime2() {
		return (formatDate(now(), "yyyyMMddHHmmss"));
	}

	public static String formatDateTime() {
		return (formatDate(now(), "yyyy-MM-dd HH:mm:ss"));
	}

	public static String formatDate(Date date) {
		return (formatDate(date, "yyyy-MM-dd"));
	}

	public static String formatDate() {
		return (formatDate(now(), "yyyy-MM-dd"));
	}

	public static String formatTime(Date date) {
		return (formatDate(date, "HH:mm:ss"));
	}

	public static String formatDateTime(String pattern) {
		return (formatDate(now(), pattern));
	}

	public static String formatTime() {
		return (formatDate(now(), "HH:mm:ss"));
	}

	public static String formatTime2() {
		return (formatDate(now(), "HHmmss"));
	}

	public static Date now() {
		return (new Date());
	}

	public static Date parseDateTime(String datetime) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if ((datetime == null) || (datetime.equals(""))) {
			return null;
		} else {
			try {
				return formatter.parse(datetime);
			} catch (ParseException e) {
				return parseDate(datetime);
			}
		}
	}

	public static Date parseDateTime(String datetime, String formate) {
		SimpleDateFormat formatter = new SimpleDateFormat(formate);
		if ((datetime == null) || (datetime.equals(""))) {
			return null;
		} else {
			try {
				return formatter.parse(datetime);
			} catch (ParseException e) {
				return parseDate(datetime);
			}
		}
	}

	public static Date parseDate(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		if ((date == null) || (date.equals(""))) {
			return null;
		} else {
			try {
				return formatter.parse(date);
			} catch (ParseException e) {
				return null;
			}
		}
	}

	public static Date parseDate(String date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);

		if ((date == null) || (date.equals(""))) {
			return null;
		} else {
			try {
				return formatter.parse(date);
			} catch (ParseException e) {
				return null;
			}
		}
	}

	public static Date parseDate(Date datetime) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		if (datetime == null) {
			return null;
		} else {
			try {
				return formatter.parse(formatter.format(datetime));
			} catch (ParseException e) {
				return null;
			}
		}
	}

	public static String formatDate(Object o) {
		if (o == null)
			return "";
		if (o.getClass() == String.class) {
			if (StringUtil.isStrEmpty((String) o)) {
				return "";
			}
			return formatDate((String) o);
		} else if (o.getClass() == Date.class)
			return formatDate((Date) o);
		else if (o.getClass() == Timestamp.class) {
			return formatDate(new Date(((Timestamp) o).getTime()));
		} else
			return o.toString();
	}

	public static String formatDateTime(Object o) {
		if (o.getClass() == String.class)
			return formatDateTime((String) o);
		else if (o.getClass() == Date.class)
			return formatDateTime((Date) o);
		else if (o.getClass() == Timestamp.class) {
			return formatDateTime(new Date(((Timestamp) o).getTime()));
		} else
			return o.toString();
	}

	public static Date add(Date date, int field, int amount) {
		if (date == null) {
			date = new Date();
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(field, amount);

		return cal.getTime();
	}

	public static Date addMilliSecond(Date date, int amount) {
		return add(date, Calendar.MILLISECOND, amount);
	}

	public static Date addSecond(Date date, int amount) {
		return add(date, Calendar.SECOND, amount);
	}

	public static Date addMiunte(Date date, int amount) {
		return add(date, Calendar.MINUTE, amount);
	}

	public static Date addHour(Date date, int amount) {
		return add(date, Calendar.HOUR, amount);
	}

	public static Date addDay(Date date, int amount) {
		return add(date, Calendar.DATE, amount);
	}

	public static Date addMonth(Date date, int amount) {
		return add(date, Calendar.MONTH, amount);
	}

	public static Date addYear(Date date, int amount) {
		return add(date, Calendar.YEAR, amount);
	}

	public static Date getDate() {
		return parseDate(formatDate());
	}

	public static Date getDateTime() {
		return parseDateTime(formatDateTime());
	}

	public static int getMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH) + 1;
	}

	public static int getYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}

	public static int getYear() {
		return getYear(new Date());
	}

	public static int getMonth() {
		return getMonth(new Date());
	}

	public static int getAge(Date birthDay) {
		Calendar cal = Calendar.getInstance();

		if (cal.before(birthDay)) {
			throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
		}

		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH);
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(birthDay);

		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				// monthNow==monthBirth
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				} else {
					// do nothing
				}
			} else {
				// monthNow>monthBirth
				age--;
			}
		} else {
			// monthNow<monthBirth
			// donothing
		}

		return age;
	}

	public static int getIntervalDays(Date beginDate, Date endDate) {
		long millisecond = endDate.getTime() - beginDate.getTime();
		int day = (int) (millisecond / 24L / 60L / 60L / 1000L);
		return day;
	}

	public static int getWeekdayOfDate(Date date) {
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(date);
		} catch (Exception e) {
			return -1;
		}
		int weekday = c.get(Calendar.DAY_OF_WEEK) - 1;
		return weekday;
	}

	public static boolean isExpired(Date date) {
		if (date.getTime() <= getDate().getTime())
			return true;
		else
			return false;
	}

	public static Date getFirstDate(Date d) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();
	}

	public static Date getLastDate(Date d) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}

	public static boolean isValidDate(String format, String dateString) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			dateFormat.setLenient(false);
			dateFormat.parse(dateString);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static int getDaysInMonth(int year, int mon) {
		java.util.GregorianCalendar date = new java.util.GregorianCalendar(year, mon, 1);
		date.add(Calendar.DATE, -1);
		return (date.get(Calendar.DAY_OF_MONTH));
	}

	public static void main(String[] args) {
		System.out.println(YunDateUtil.getDaysInMonth(2013, 4));
	}
}
