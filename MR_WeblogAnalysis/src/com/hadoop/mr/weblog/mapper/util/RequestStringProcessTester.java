package com.hadoop.mr.weblog.mapper.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hadoop.mr.weblog.mapper.WeblogParseMapper;

public class RequestStringProcessTester {

	private static int TOTAL_COUNT = 9;

	private static String TAB_DELIM = "\t";
	private static int REQUEST_STRING  = 5;
	

	public static void main(String[] args) throws ClassNotFoundException {
		WeblogParseMapper mapper = new WeblogParseMapper();
		
		try {
			String A_SPACE = " ";
			StringBuilder weblogPatternBuilder = new StringBuilder();

			// 50.57.190.149 - - [22/Apr/2012:07:12:42 +0530] "GET /a/b/c/d?p=10 HTTP/1.0"
			// 200 12530 "-" "-"
			String weblogPattern = weblogPatternBuilder.append(RegexUtil.REG_START).append(RegexUtil.REG_IP_ADDR)
					.append(A_SPACE).append(RegexUtil.REG_ANY_STRING).append(A_SPACE).append(RegexUtil.REG_ANY_STRING)
					.append(A_SPACE).append(RegexUtil.REG_DATETIME_IN_SQUARE_BRACKET).append(A_SPACE)
					.append(RegexUtil.REG_DOUBLE_QUOTED_STRING).append(A_SPACE).append(RegexUtil.REG_ANY_STRING)
					.append(A_SPACE).append(RegexUtil.REG_ANY_STRING).append(A_SPACE)
					.append(RegexUtil.REG_DOUBLE_QUOTED_STRING).append(A_SPACE)
					.append(RegexUtil.REG_DOUBLE_QUOTED_STRING).append(RegexUtil.REG_END).toString();

			Pattern pattern = Pattern.compile(weblogPattern);

			Class cls = Class.forName("com.hadoop.mr.weblog.mapper.util.RequestStringProcessTester");

	        // returns the ClassLoader object associated with this Class
	        ClassLoader cLoader = cls.getClassLoader();
	         
	        // input stream
	        InputStream i = cLoader.getResourceAsStream("weblogs_1_thousand_rec.txt");
	        BufferedReader reader = new BufferedReader(new InputStreamReader(i));

			
			String line = reader.readLine();
			while (line != null) {
				String weblog = line;
				weblog = weblog.trim().replace(TAB_DELIM, " ");

				Matcher matcher = pattern.matcher(weblog);

				if (matcher.matches() && matcher.groupCount() == TOTAL_COUNT) {

					String requestString = matcher.group(REQUEST_STRING);
					String tabSeparateReqCategory = mapper.getTabSeparatedReqCategories(requestString);

					System.out.println(tabSeparateReqCategory);
				}
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
