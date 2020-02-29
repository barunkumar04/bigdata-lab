package com.hadoop.mr.hr.analytics.reducer;

import java.io.IOException;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class DeptWiseAvgMonthlyHourReducer extends Reducer<Text, FloatWritable, Text, FloatWritable> {

	@Override
	protected void reduce(Text dept, Iterable<FloatWritable> satisfactionLevels, Context context) throws IOException, InterruptedException {
		Float sumSatisfactionLevels = Float.valueOf(0);
		int count = 0;
		for (FloatWritable satisfactionLevel : satisfactionLevels) {
			sumSatisfactionLevels = sumSatisfactionLevels + satisfactionLevel.get();
			count ++;
		}
		
		FloatWritable avgSatisfactionLevel = new FloatWritable(sumSatisfactionLevels / count);
		context.write(dept, avgSatisfactionLevel);
	}
	
	
}
