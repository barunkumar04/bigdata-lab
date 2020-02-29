package com.hadoop.mr.weblog.mapper.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtilTester {

	public static void main(String[] args) {
		String A_SPACE = " ";
		StringBuilder weblogPatternBuilder = new StringBuilder();
		
		//50.57.190.149 - - [22/Apr/2012:07:12:42 +0530] "GET /a/b/c/d?p=10 HTTP/1.0" 200 12530 "-" "-"
		String weblogPattern = weblogPatternBuilder.append(RegexUtil.REG_START)
												.append(RegexUtil.REG_IP_ADDR).append(A_SPACE)
												.append(RegexUtil.REG_ANY_STRING).append(A_SPACE)
												.append(RegexUtil.REG_ANY_STRING).append(A_SPACE)
												.append(RegexUtil.REG_DATETIME_IN_SQUARE_BRACKET).append(A_SPACE)
												.append(RegexUtil.REG_DOUBLE_QUOTED_STRING).append(A_SPACE)
												.append(RegexUtil.REG_ANY_STRING).append(A_SPACE)
												.append(RegexUtil.REG_ANY_STRING).append(A_SPACE)
												.append(RegexUtil.REG_DOUBLE_QUOTED_STRING).append(A_SPACE)
												.append(RegexUtil.REG_DOUBLE_QUOTED_STRING)
												.append(RegexUtil.REG_END)
												.toString();
		
		String weblog = "50.57.190.149 - - [22/Apr/2012:07:12:42 +0530] \"GET /a/b/c/d?p=10 HTTP/1.0\" 200 12530 \"-\" \"-\"";
		weblog = weblog.replace("\t", " ");
		Pattern pattern = Pattern.compile(weblogPattern);
		
		Matcher matcher = pattern.matcher(weblog);
		
		if (matcher.matches()){
			System.out.println("Matched");
			System.out.println("IP: "+matcher.group(1));
			System.out.println("Request: "+matcher.group(2));
			System.out.println("User: "+matcher.group(3));
			System.out.println("DateTime: "+matcher.group(4));
			System.out.println("Request Details: "+matcher.group(5));
			System.out.println("Status code: "+matcher.group(6));
			System.out.println("Byte String: "+matcher.group(7));
			System.out.println("User string: "+matcher.group(8));
			System.out.println("Referral: "+matcher.group(9));
			
		}else {
			System.out.println("Not matched.");
		}
		
	}

}
