package com.hadoop.mr.hr.analytics.reducer;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import com.hadoop.mr.hr.analytics.util.HRUtil;
import com.hadoop.mr.hr.analytics.writable.HRResourceWritable;

public class BasedOnSalaryCategoryDeptWiseReportReducer extends Reducer<Text, HRResourceWritable, Text, Text> {

	@Override
	protected void reduce(Text dept, Iterable<HRResourceWritable> hrResources, Context context) throws IOException, InterruptedException {
		Integer resignationCount = Integer.valueOf(0);
		Float sumSatisfactionLevel = Float.valueOf(0);
		Float sumEvaluation = Float.valueOf(0);
		int count = 0;
		for (HRResourceWritable hrResource : hrResources) {
			if(hrResource.getSalaryCategory().toString().equalsIgnoreCase(HRUtil.SAL_LOW)) {
				continue;
			}
			if(hrResource.getHasResigned().get()) {
				resignationCount++;
			}
			sumSatisfactionLevel = sumSatisfactionLevel + hrResource.getSatisfactionLevel().get();
			sumEvaluation = sumEvaluation + hrResource.getEvaluation().get();
			count++;
		}
		
		Float avgSatisfactionLevel = sumEvaluation / count;
		Float avgEvaluation = sumEvaluation / count;
		Float percentageResignation = (float) ((resignationCount * 100.0) / count);
		
		context.write(dept, new Text(avgSatisfactionLevel+"\t"+avgEvaluation+"\t"+percentageResignation));
	}
	
	
}
