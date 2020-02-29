package com.hadoop.mr.hr.analytics.reducer;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class DepartmentWiseSalaryDistributionReducer extends Reducer<Text, Text, Text, IntWritable> {

	@Override
	protected void reduce(Text dept, Iterable<Text> salCategory, Context context) throws IOException, InterruptedException {
		
		int counter = 0;
		for (Object i : salCategory) {
		    counter++;
		}
		
		context.write(dept, new IntWritable(counter));
	}
	
	
}
