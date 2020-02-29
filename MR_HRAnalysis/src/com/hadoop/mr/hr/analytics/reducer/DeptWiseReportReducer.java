package com.hadoop.mr.hr.analytics.reducer;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import com.hadoop.mr.hr.analytics.writable.HRResourceWritable;

public class DeptWiseReportReducer extends Reducer<Text, HRResourceWritable, Text, Text> {

	@Override
	protected void reduce(Text dept, Iterable<HRResourceWritable> hrResources, Context context) throws IOException, InterruptedException {
		Integer resignationCount = Integer.valueOf(0);
		Float sumSatisfactionLevel = Float.valueOf(0);
		Float avgWorkingHours = Float.valueOf(0);
		int count = 0;
		for (HRResourceWritable hrResource : hrResources) {
			if(hrResource.getHasResigned().get()) {
				resignationCount++;
			}
			sumSatisfactionLevel = sumSatisfactionLevel + hrResource.getAvgSatisfactionLevel().get();
			avgWorkingHours = avgWorkingHours + hrResource.getAvgWorkingHours().get();
			count++;
		}
		
		context.write(dept, new Text(sumSatisfactionLevel/count+"\t"+avgWorkingHours+"\t"+resignationCount));
	}
	
	
}
