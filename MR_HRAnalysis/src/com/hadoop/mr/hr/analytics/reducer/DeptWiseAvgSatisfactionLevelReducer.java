package com.hadoop.mr.hr.analytics.reducer;

import java.io.IOException;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class DeptWiseAvgSatisfactionLevelReducer extends Reducer<Text, FloatWritable, Text, FloatWritable> {

	@Override
	protected void reduce(Text dept, Iterable<FloatWritable> avgMonthlyHours, Context context) throws IOException, InterruptedException {
		Float avgMonthlyHour = Float.valueOf(0);
		
		for (FloatWritable monthlyHour : avgMonthlyHours) {
			avgMonthlyHour = avgMonthlyHour + monthlyHour.get();
		}
		
		context.write(dept, new FloatWritable(avgMonthlyHour));
	}
	
	
}
