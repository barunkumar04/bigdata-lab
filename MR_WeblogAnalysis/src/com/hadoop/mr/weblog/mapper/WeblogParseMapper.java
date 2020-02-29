package com.hadoop.mr.weblog.mapper;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

import com.hadoop.mr.weblog.mapper.util.RegexUtil;
import com.hadoop.mr.weblog.mapper.util.WeblogUtils;

public class WeblogParseMapper extends Mapper<Object, Text, NullWritable, Text> {

	private Pattern pattern = null;
	private int REMOTE_IP = 1;
	private int REMOTE_LOG_NAME = 2;
	private int REMOTE_USER = 3;
	private int TIME = 4;
	private int REQUEST_STRING  = 5;
	private int STATUS_CODE  = 6;
	private int BYTE_STRING = 7;
	private int USER_AGENT = 8;
	private int REFERRAL = 9; 
	
	private int TOTAL_COUNT = 9;
	
	private int MAX_CAT_HIERARCHY = 5;
	private String TAB_DELIM= "\t";
	private String DASH = " - ";
	private StringBuilder parsedWeblog;
	
	private MultipleOutputs<NullWritable, Text> multiOuts = null;
	
	@Override
	protected void setup(Context context) throws IOException, InterruptedException {
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
		
		pattern = Pattern.compile(weblogPattern);
		
		multiOuts = new MultipleOutputs<NullWritable, Text>(context);
	}
	
	
	
	@Override
	protected void cleanup(Context context)
			throws IOException, InterruptedException {
		multiOuts.close();
	}



	@Override
	protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		
		String weblog = value.toString();
		weblog = weblog.trim().replace(TAB_DELIM," ");
		
		Matcher matcher = pattern.matcher(weblog);
		
		if(matcher.matches() && matcher.groupCount() == TOTAL_COUNT){
		
			String requestString = matcher.group(REQUEST_STRING);
			String tabSeparateReqCategory = getTabSeparatedReqCategories(requestString);
			
			String ipAddr = matcher.group(REMOTE_IP);
			String remoteLoginName = matcher.group(REMOTE_LOG_NAME);
			String remoteUser = matcher.group(REMOTE_USER);
			String time = matcher.group(TIME);
			String statusCode = matcher.group(STATUS_CODE);
			String byteString = matcher.group(BYTE_STRING);
			String userString = matcher.group(USER_AGENT);
			String referral = matcher.group(REFERRAL);
			
			parsedWeblog = new StringBuilder();
			
			parsedWeblog = parsedWeblog.append(ipAddr).append(TAB_DELIM)
					.append(remoteLoginName).append(TAB_DELIM)
					.append(remoteUser).append(TAB_DELIM)
					.append(time).append(TAB_DELIM)
					.append(tabSeparateReqCategory).append(TAB_DELIM)
					.append(statusCode).append(TAB_DELIM)
					.append(byteString).append(TAB_DELIM)
					.append(userString).append(TAB_DELIM)
					.append(referral);
			
			multiOuts.write(WeblogUtils.MULTI_OUT_PARSED_REC, NullWritable.get(), new Text(parsedWeblog.toString()));
		}else {
			multiOuts.write(WeblogUtils.MULTI_OUT_INVALID_REC, NullWritable.get(), value);
		}
		
	}
	
	public String getTabSeparatedReqCategories(String requestString) {
		MAX_CAT_HIERARCHY = 5;
		/**
		 * Possible types of request strings and its expected outputs:
		 * 
		 * "GET /a/b/c/d/page.html?param=aValue HTTP/1.0" -> GET	/a/b/c/d/page.html?param=aValue	a	b	c	d	page.html	param=aValue
		 * "GET /a/b/page.html?param=aValue HTTP/1.0"     -> GET	/a/b/page.html?param=aValue	a	b	-	-	page.html	param=aValue
		 * "GET /a/b/page.html HTTP/1.0"     			  -> GET	/a/b/page.html?param=aValue	a	b	-	-	page.html
		 * "GET / HTTP/1.1"                               -> GET	/	-	-	-	-
		 * "GET /page.html"                               -> GET	/page.html	-	-	-	-	page.html
		 * 
		 */
		
		String[] splits = requestString.split(" ");
		
		StringBuilder formattedRequestString = new StringBuilder();
		formattedRequestString = formattedRequestString.append(splits[0]).append(TAB_DELIM).append(splits[1]).append(TAB_DELIM);
		
		// Processing for category hierarchies and param, i.e. /a/b/c/d/page.html?param=aValue and similar.
		String[] categoriesAndParamSplit = splits[1].split("\\?");
		
		
		// Processing for category hierarchies, i.e. /a/b/c/d/page.html, /a/page.html, / etc 
		String[] categoriesSplit = categoriesAndParamSplit[0].split("/");
		
		
		int categoriesSplitCount = categoriesSplit.length;
		//additional handling for / case
		if(categoriesSplitCount == 0 ) {
			formattedRequestString =  formattedRequestString.append("/").append(TAB_DELIM);
			MAX_CAT_HIERARCHY = MAX_CAT_HIERARCHY - 1;
		}
		//for /a/b/page.html case
		for(int i=1; i<=MAX_CAT_HIERARCHY ; i++) {
			if(i <= (categoriesSplitCount - 2)) { // excluding first blank string and last page.html
				formattedRequestString = formattedRequestString.append(categoriesSplit[i]).append(TAB_DELIM);
			}else if(i == MAX_CAT_HIERARCHY && categoriesSplitCount != 0) {//This the page.html
				formattedRequestString = formattedRequestString.append(categoriesSplit[categoriesSplitCount-1]);
			}else {
				formattedRequestString = formattedRequestString.append(DASH).append(TAB_DELIM);
			}
		}
		
		// if parameter has provided
		if(categoriesAndParamSplit.length == 2) {
			formattedRequestString = formattedRequestString.append(TAB_DELIM).append(categoriesAndParamSplit[1]);
		}
		
		return formattedRequestString.toString();
	}
	
	
	}
