package com.hadoop.mr.weblog.mapper.util;

public class RegexUtil {
	
	//50.57.190.149 - - [22/Apr/2012:07:12:42 +0530] "GET /a/b/c/d?p=10 HTTP/1.0" 200 12530 "-" "-"
	
	public static final String REG_START = "^";
	public static final String REG_IP_ADDR = "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})";
	public static final String REG_ANY_STRING = "(\\S+)";
	public static final String REG_DATETIME_IN_SQUARE_BRACKET = "\\[(.+?)\\]";
	public static final String REG_DOUBLE_QUOTED_STRING = "\"([^\"]*)\"";
	public static final String REG_END = "$";
}
