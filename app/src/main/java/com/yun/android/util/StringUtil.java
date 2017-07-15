// Source File Name: StringUtil.java

package com.yun.android.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Referenced classes of package com.aplikata.util:
// ArithUtil

public class StringUtil {
	public final static int RADOM_TY_NUMBER = 0;
	public final static int RADOM_TY_CHAR = 1;
	public final static int RADOM_TY_CHAR_NUMBER = 2;

	public StringUtil() {
	}

	public static String trimLeft(String value) {
		if (value == null)
			return "";
		String result = value;
		char ch[] = result.toCharArray();
		int index = -1;
		for (int i = 0; i < ch.length; i++) {
			if (!Character.isWhitespace(ch[i]))
				break;
			index = i;
		}

		if (index != -1)
			result = result.substring(index + 1);
		return result;
	}

	public static String trimRight(String value) {
		if (value == null)
			return "";
		String result = value;
		char ch[] = result.toCharArray();
		int endIndex = -1;
		for (int i = ch.length - 1; i > -1; i--) {
			if (!Character.isWhitespace(ch[i]))
				break;
			endIndex = i;
		}

		if (endIndex != -1)
			result = result.substring(0, endIndex);
		return result;
	}

	public static String fillHeadCharsLen(String strOri, int len) {
		return fillHeadCharsLen(strOri, "0", len);
	}

	public static String fillBackCharsLen(String strOri, int len) {
		return fillBackCharsLen(strOri, "0", len);
	}

	public static String fillHeadCharsLen(String strOri, String subStr, int len) {
		if (strOri == null || strOri.trim().length() == 0)
			strOri = "";
		if (subStr == null)
			subStr = " ";
		String fillStr = "";
		for (int i = 0; i < len; i++)
			fillStr = (new StringBuilder(String.valueOf(fillStr))).append(subStr).toString();

		subStr = (new StringBuilder(String.valueOf(fillStr))).append(strOri).toString();
		return subStr.substring(subStr.length() - len, subStr.length());
	}

	public static String fillBackCharsLen(String strOri, String subStr, int len) {
		if (strOri == null || strOri.trim().length() == 0)
			strOri = "";
		if (subStr == null)
			subStr = " ";
		String fillStr = "";
		for (int i = 0; i < len; i++)
			fillStr = (new StringBuilder(String.valueOf(fillStr))).append(subStr).toString();

		subStr = (new StringBuilder(String.valueOf(strOri))).append(fillStr).toString();
		return subStr.substring(0, len);
	}

	public static String fillHeadChars(String strOri, int counter) {
		return fillHeadChars(strOri, "0", counter);
	}

	public static String fillBackChars(String strOri, int counter) {
		return fillBackChars(strOri, "0", counter);
	}

	public static String fillHeadChars(String strOri, String subStr, int counter) {
		if (strOri == null || strOri.trim().length() == 0)
			strOri = "";
		if (counter <= 0 || subStr == null)
			return strOri;
		String fillStr = "";
		for (int i = 0; i < counter; i++)
			fillStr = (new StringBuilder(String.valueOf(fillStr))).append(subStr).toString();

		return (new StringBuilder(String.valueOf(fillStr))).append(strOri).toString();
	}

	public static String fillBackChars(String strOri, String subStr, int counter) {
		if (strOri == null || strOri.trim().length() == 0)
			strOri = "";
		if (counter <= 0 || subStr == null)
			return strOri;
		String fillStr = "";
		for (int i = 0; i < counter; i++)
			fillStr = (new StringBuilder(String.valueOf(fillStr))).append(subStr).toString();

		return (new StringBuilder(String.valueOf(strOri))).append(fillStr).toString();
	}

	public static boolean isEmpty(Object strObj) {
		return strObj == null || strObj.toString().trim().length() < 1;
	}

	public static boolean isStrEmpty(String str) {
		return str == null || str.trim().length() < 1;
	}

	public static String getValue(String str) {
		if (str == null)
			return "";
		if (str.trim().length() <= 0) {
			return "";
		} else {
			str = (new StringBuilder("H")).append(str).toString();
			str = str.trim();
			str = str.substring(1);
			return str;
		}
	}

	public static boolean chkTextLen(String text, int len) {
		return text != null && text.length() <= len;
	}

	public static boolean chkTextTrimLen(String text, int len) {
		return text != null && text.trim().length() <= len;
	}

	public static boolean isStrEn(String text) {
		for (int i = 0; i < text.length(); i++)
			if (text.charAt(i) > '\177')
				return false;

		return true;
	}

	public static boolean isCharNum(char ch) {
		return ch > '/' && ch < ':';
	}

	public static boolean isStrNum(String str) {
		if (isStrEmpty(str))
			return true;
		boolean notNum = false;
		for (int i = 0; i < str.length(); i++)
			if (!isCharNum(str.charAt(i)))
				notNum = true;

		return !notNum;
	}

	public static boolean isNum(String strSrc) throws Exception {
		for (int i = 0; i < strSrc.length(); i++)
			if (!isCharNum(strSrc.charAt(i)))
				return false;

		return true;
	}

	public static boolean isCharLetter(char ch) {
		return ch >= 'A' && ch <= 'Z' && ch >= 'a' && ch <= 'z';
	}

	public static boolean isStrLetter(String str) {
		if (isStrEmpty(str))
			return true;
		boolean notLetter = false;
		for (int i = 0; i < str.length(); i++)
			if (!isCharLetter(str.charAt(i)))
				notLetter = true;

		return !notLetter;
	}

	public String nullToSpace(String Content) {
		if (Content == null)
			Content = "";
		return Content;
	}

	public static char strToChar(String src) {
		src = src.trim();
		char result = src.charAt(0);
		return result;
	}

	public static String encodeSQL(String sql) {
		StringBuffer tempBuff = new StringBuffer();
		for (int i = 0; i < sql.length(); i++)
			tempBuff.append(Integer.toHexString(sql.charAt(i)));

		return tempBuff.toString();
	}

	public static String decodeSQL(String encoded) {
		StringBuffer tempBuff = new StringBuffer();
		for (int i = 0; i < encoded.length(); i += 2)
			tempBuff.append((char) Integer.parseInt(encoded.substring(i, i + 2), 16));

		return tempBuff.toString();
	}

	public static String getAbsolutePath(String path1, String context1) {
		int i1 = path1.indexOf(context1);
		if (i1 < 0)
			return path1;
		else
			return path1.substring(path1.indexOf(context1) + context1.length());
	}

	public static String getSubString(String str1, int sindex, int eindex) {
		if (str1 == null)
			return "";
		if (str1.trim().length() <= 0)
			return "";
		if (str1.length() > sindex) {
			if (eindex >= 0)
				return str1.substring(sindex, eindex);
			if (eindex < 0)
				return str1.substring(sindex);
		}
		return "";
	}

	public static String[] getValues(String strs[], int size1) {
		String strs1[] = new String[size1];
		for (int i = 0; i < size1; i++)
			strs1[i] = "";

		if (strs == null)
			return strs1;
		if (strs.length < size1) {
			for (int i = 0; i < strs.length; i++)
				strs1[i] = strs[i];

			return strs1;
		} else {
			return strs;
		}
	}

	public static String replaceStrAll(String strSource, String strFrom, String strTo) {
		String strDest = "";
		int intFromLen = strFrom.length();
		int intPos;
		while ((intPos = strSource.indexOf(strFrom)) != -1) {
			strDest = (new StringBuilder(String.valueOf(strDest))).append(strSource.substring(0, intPos)).toString();
			strDest = (new StringBuilder(String.valueOf(strDest))).append(strTo).toString();
			strSource = strSource.substring(intPos + intFromLen);
		}
		strDest = (new StringBuilder(String.valueOf(strDest))).append(strSource).toString();
		return strDest;
	}

	public static String replaceStr(String strTarget, String strNew) {
		int iIndex = -1;
		do {
			iIndex = strTarget.indexOf('\n');
			if (iIndex >= 0) {
				String strTemp = null;
				strTemp = strTarget.substring(0, iIndex);
				strTarget = (new StringBuilder(String.valueOf(strTemp))).append(strNew)
						.append(strTarget.substring(iIndex + 1)).toString();
			} else {
				return strTarget;
			}
		} while (true);
	}

	public static boolean includestr(String str1, String strarray[]) {
		if (strarray == null || strarray.length <= 0)
			return false;
		for (int i = 0; i < strarray.length; i++)
			if (strarray[i] == null) {
				if (str1 == null)
					return true;
			} else if (strarray[i].trim().equals(str1))
				return true;

		return false;
	}

	public static String[] getAreaValues(String fvalue) {
		String tmpstr = fvalue;
		int i = 0;
		if (tmpstr == null)
			return null;
		if (tmpstr.trim().equals(""))
			return null;
		for (; tmpstr.indexOf("\n") >= 0; tmpstr = tmpstr.substring(tmpstr.indexOf("\n") + 1))
			i++;

		if (tmpstr.trim().equals(""))
			i--;
		String fvalues[] = new String[i + 1];
		tmpstr = fvalue;
		i = 0;
		for (; tmpstr.indexOf("\n") >= 0; tmpstr = tmpstr.substring(tmpstr.indexOf("\n") + 1)) {
			fvalues[i] = tmpstr.substring(0, tmpstr.indexOf("\n"));
			if (fvalues[i].indexOf("\r") >= 0)
				fvalues[i] = fvalues[i].substring(0, fvalues[i].indexOf("\r"));
			i++;
		}

		if (!tmpstr.trim().equals(""))
			fvalues[i] = tmpstr;
		return fvalues;
	}

	public static String getrealAreaValues(String fvalue) {
		String tmpstr = fvalue;
		String returnstr = "";
		if (tmpstr == null)
			return null;
		if (tmpstr.trim().equals(""))
			return "";
		for (; tmpstr.indexOf("|") > 0; tmpstr = tmpstr.substring(tmpstr.indexOf("|") + 1))
			returnstr = (new StringBuilder(String.valueOf(returnstr))).append(tmpstr.substring(0, tmpstr.indexOf("|")))
					.append("\n").toString();

		return returnstr;
	}

	public static int countChar(String strInput, char chr) {
		int iCount = 0;
		char chrTmp = ' ';
		if (strInput.trim().length() == 0)
			return 0;
		for (int i = 0; i < strInput.length(); i++) {
			chrTmp = strInput.charAt(i);
			if (chrTmp == chr)
				iCount++;
		}

		return iCount;
	}

	public static String strArrayToStr(String strs[]) {
		return strArrayToStr(strs, null);
	}

	public static void printStrs(String strs[]) {
		for (int i = 0; i < strs.length; i++)
			System.out.println(strs[i]);

	}

	public static void printDualStr(String dualStr[][]) {
		for (int i = 0; i < dualStr.length; i++) {
			for (int j = 0; j < dualStr[i].length; j++)
				System.out.print((new StringBuilder(String.valueOf(dualStr[i][j]))).append(" ").toString());

			System.out.println();
		}

	}

	public static String[][] rowToColumn(String dualStr[][]) {
		String returnDualStr[][] = (String[][]) null;
		if (dualStr != null) {
			returnDualStr = new String[dualStr[0].length][dualStr.length];
			for (int i = 0; i < dualStr.length; i++) {
				for (int j = 0; j < dualStr[0].length; j++)
					returnDualStr[j][i] = dualStr[i][j];

			}

		}
		return returnDualStr;
	}

	public static String latinString(String inStr) {
		String res = inStr;
		if (res == null) {
			return null;
		} else {
			res = replaceStrAll(res, "\"", "\\\"");
			res = replaceStrAll(res, "'", "\\'");
			return res;
		}
	}

	public static String replaceWhiteSpace(String strTarget, String strNew) {
		int iIndex = -1;
		do {
			char cRep = ' ';
			iIndex = strTarget.indexOf(cRep);
			if (iIndex >= 0) {
				String strTemp = null;
				strTemp = strTarget.substring(0, iIndex);
				strTarget = (new StringBuilder(String.valueOf(strTemp))).append(strNew)
						.append(strTarget.substring(iIndex + 1)).toString();
			} else {
				return strTarget;
			}
		} while (true);
	}

	public static String double2str(double amount, int length) {
		String strAmt = Double.toString(amount);
		int pos = strAmt.indexOf('.');
		if (pos != -1 && strAmt.length() > length + pos + 1)
			strAmt = strAmt.substring(0, pos + length + 1);
		return strAmt;
	}

	public static String[] doSplit(String str, char chr) {
		int iCount = 0;
		char chrTmp = ' ';
		for (int i = 0; i < str.length(); i++) {
			chrTmp = str.charAt(i);
			if (chrTmp == chr)
				iCount++;
		}

		String strArray[] = new String[iCount];
		for (int i = 0; i < iCount; i++) {
			int iPos = str.indexOf(chr);
			if (iPos == 0)
				strArray[i] = "";
			else
				strArray[i] = str.substring(0, iPos);
			str = str.substring(iPos + 1);
		}

		return strArray;
	}

	public static String[] strSplit(String src, String splitchar) {
		int resultSize = 0;
		int len = src.length();
		int idx = 0;
		String strTemp = "";
		for (int i = 0; i < len; i++)
			if (src.substring(i, i + 1).equals(splitchar))
				resultSize++;

		if ((len > 1) & (!src.substring(len - 1, len).equals(splitchar)))
			resultSize++;
		String result[] = new String[resultSize];
		for (int i = 0; i < len; i++)
			if (src.substring(i, i + 1).equals(splitchar)) {
				result[idx] = strTemp;
				idx++;
				strTemp = "";
			} else {
				strTemp = (new StringBuilder(String.valueOf(String.valueOf(strTemp)))).append(
						String.valueOf(src.charAt(i))).toString();
			}

		if (!strTemp.equals(""))
			result[idx] = strTemp;
		return result;
	}

	public static String[] split(String strToSplit, String strSeparator, int iLimit) {
		ArrayList<String> tmpList = new ArrayList<String>();
		int iFromIndex = 0;
		int iCurIndex = strToSplit.length();
		String strUnitInfo = "";
		for (int iCurCounts = 0; iCurIndex != -1 && iFromIndex < strToSplit.length() && iCurCounts < iLimit; iCurCounts++) {
			iCurIndex = strToSplit.indexOf(strSeparator, iFromIndex);
			if (iCurIndex == -1) {
				strUnitInfo = strToSplit.substring(iFromIndex, strToSplit.length());
			} else {
				strUnitInfo = strToSplit.substring(iFromIndex, iCurIndex);
				iFromIndex = iCurIndex + 1;
			}
			tmpList.add(strUnitInfo);
		}

		int iCounts = tmpList.size();
		String tmpArray[] = new String[iCounts];
		for (int i = 0; i < iCounts; i++)
			tmpArray[i] = (String) tmpList.get(i);

		return tmpArray;
	}

	public static String strIntercept(String src, int len) {
		if (src == null)
			return "";
		if (src.length() > len)
			src = String.valueOf(String.valueOf(src.substring(0, len))).concat("...");
		return src;
	}

	public static String strtochn(String str_in) {
		try {
			String temp_p = str_in;
			if (temp_p == null)
				temp_p = "";
			String temp = "";
			if (!temp_p.equals("")) {
				byte byte1[] = temp_p.getBytes("ISO8859_1");
				temp = new String(byte1);
			}
			return temp;
		} catch (Exception exception) {
			return "null";
		}
	}

	public static String ISO2GBK(String strvalue) {
		if (strvalue == null)
			return null;
		try {
			strvalue = new String(strvalue.getBytes("ISO8859_1"), "GBK");
			return strvalue;
		} catch (Exception e) {
			return null;
		}
	}

	public String GBK2ISO(String strvalue) throws Exception {
		if (strvalue == null)
			return null;
		try {
			strvalue = new String(strvalue.getBytes("GBK"), "ISO8859_1");
			return strvalue;
		} catch (Exception e) {
			return null;
		}
	}

	public static String cnCodeTrans(String str) {
		String s = "";
		try {
			s = new String(str.getBytes("GB2312"), "8859_1");
		} catch (UnsupportedEncodingException a) {
			System.out.print("chinese thansform exception");
		}
		return s;
	}

	public static boolean judgeMatch(String strSource, String strRule) {
		int i = 0;
		if (strSource == null || strSource.length() == 0)
			return false;
		if (strRule == null || strRule.length() == 0)
			return false;
		if (strSource.length() > strRule.length())
			return false;
		for (i = 0; i < strRule.length(); i++) {
			if (strSource.length() < i + 1)
				break;
			if (strRule.charAt(i) != '*' && strSource.charAt(i) != strRule.charAt(i))
				return false;
		}

		for (; i < strRule.length(); i++)
			if (strRule.charAt(i) != '*')
				return false;

		return true;
	}

	public static String column2Property(String column) {
		column = column.toLowerCase();
		for (int i = column.indexOf("_"); i != -1; i = column.indexOf("_")) {
			if (i == column.length() - 1)
				break;
			char temp = column.charAt(i + 1);
			String strTemp = String.valueOf(temp);
			column = column.replaceFirst((new StringBuilder("_")).append(strTemp).toString(), strTemp.toUpperCase());
		}

		return column;
	}

	public static String strArrayToStr(String strs[], String separator) {
		StringBuffer returnstr = new StringBuffer("");
		if (strs == null)
			return "";
		if (separator == null)
			separator = "";
		for (int i = 0; i < strs.length; i++) {
			returnstr.append(strs[i]);
			if (i < strs.length - 1)
				returnstr.append(separator);
		}

		return returnstr.toString();
	}

	public static String strArrayToStr(String strs[], String separator, String rounder) {
		StringBuffer returnstr = new StringBuffer("");
		if (strs == null)
			return "";
		if (separator == null)
			separator = "";
		for (int i = 0; i < strs.length; i++) {
			returnstr.append(rounder + strs[i] + rounder);
			if (i < strs.length - 1)
				returnstr.append(separator);
		}

		return returnstr.toString();
	}

	public static String objectArrayToStr(Object objects[], String separator) {
		StringBuffer returnstr = new StringBuffer("");
		if (objects == null)
			return "";
		if (separator == null)
			separator = "";
		for (int i = 0; i < objects.length; i++) {
			returnstr.append(String.valueOf(objects[i]));
			if (i < objects.length - 1)
				returnstr.append(separator);
		}

		return returnstr.toString();
	}

	public static String listToStr(List<?> element, String separator) {
		StringBuffer returnstr = new StringBuffer("");
		if (element == null)
			return "";

		if (StringUtil.isStrEmpty(separator))
			separator = ",";

		for (Iterator<?> it = element.iterator(); it.hasNext();) {
			returnstr.append(String.valueOf(it.next()));
			if (it.hasNext())
				returnstr.append(separator);
		}

		if (returnstr.toString().endsWith(separator)) {
			returnstr = new StringBuffer(returnstr.substring(0, returnstr.length() - 1));
		}

		return returnstr.toString();
	}

	public static String[] listToStrArray(List<?> element) {
		if (element == null || element.size() == 0)
			return null;
		Iterator<?> it = element.iterator();
		String strArray[] = new String[element.size()];
		for (int i = 0; it.hasNext(); i++)
			strArray[i] = String.valueOf(it.next());

		return strArray;
	}

	public static List<String> strToList(String str, String separator) {
		if (str == null || str.equals(""))
			return null;
		if (separator == null)
			separator = "";
		String strArr[] = str.split(separator);
		int size = strArr.length;
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < size; i++)
			list.add(strArr[i]);

		return list;
	}

	public static StringBuffer populate(StringBuffer bf, String value, boolean isNotLast) {
		if (value == null)
			return bf;
		bf.append("'").append(value.replaceAll("'", "''")).append("'");
		if (isNotLast)
			bf.append(",");
		return bf;
	}

	public static boolean isExist(String str, String substr, String sepatator) {
		if (str == null || str.trim().equals(""))
			return false;
		if (substr == null || substr.trim().equals(""))
			return false;
		String strArr[] = str.split(sepatator);
		int size = strArr.length;
		for (int i = 0; i < size; i++)
			if (strArr[i].equals(substr))
				return true;

		return false;
	}

	public static boolean isExist(String str, String substr) {
		return isExist(str, substr, ",");
	}

	public static boolean containsString(String str, String pattern) {
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(str);
		if (m.find())
			return true;
		return false;
	}

	public static String leftInclude(String str) {
		if (str == null || str.equals(""))
			return str;
		else
			return (new StringBuilder(String.valueOf(str))).append("%").toString();
	}

	public static String rightInclude(String str) {
		if (str == null || str.equals(""))
			return str;
		else
			return (new StringBuilder("%")).append(str).toString();
	}

	public static String include(String str) {
		if (str == null || str.equals(""))
			return str;
		else
			return (new StringBuilder("%")).append(str).append("%").toString();
	}

	public static String getRandomValue(int length, int type) {
		String strRandom = "";
		Random rnd = new Random();
		if (length < 0) {
			length = 5;
		}

		if ((type > 2) || (type < 0)) {
			type = 2;
		}

		switch (type) {
		case RADOM_TY_NUMBER:
			for (int iLoop = 0; iLoop < length; iLoop++) {
				strRandom += Integer.toString(rnd.nextInt(10));
			}
			break;
		case RADOM_TY_CHAR:
			for (int iLoop = 0; iLoop < length; iLoop++) {
				strRandom += Integer.toString((35 - rnd.nextInt(10)), 36);
			}
			break;
		case RADOM_TY_CHAR_NUMBER:
			for (int iLoop = 0; iLoop < length; iLoop++) {
				strRandom += Integer.toString(rnd.nextInt(36), 36);
			}
			break;
		}

		return strRandom;

	}

	public static String arraysToString(Object[] objs) {
		String string = Arrays.toString(objs).replaceAll("[\\[\\]\\s]", "");
		return string;
	}
	
	public static Long[] arrayStrToLong(String[] strings){
		Long[] longs = new Long[strings.length]; 
		for(int i=0; i<strings.length; i++){
			longs[i] = Long.parseLong(strings[i]);
		}
		return longs;
	}

	public static void main(String args[]) {
//		BufferedReader br = null;
//		String source;
//		try {
//			URL url = new URL("http://localhost:8080/aplikata_evok/LoginAction");
//			URLConnection conn = url.openConnection();
//			br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//			while ((source = br.readLine()) != null) {
//				System.out.println(source);
//			}
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (br != null) {
//					br.close();
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
		System.out.println(isStrNum("5.5"));
	}
}
