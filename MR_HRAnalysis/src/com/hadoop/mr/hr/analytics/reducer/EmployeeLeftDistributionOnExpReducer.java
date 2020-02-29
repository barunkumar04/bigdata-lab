package com.hadoop.mr.hr.analytics.reducer;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import com.hadoop.mr.hr.analytics.writable.HRResourceWritable;

public class EmployeeLeftDistributionOnExpReducer extends Reducer<Text, HRResourceWritable, Text, IntWritable> {

	@Override
	protected void reduce(Text dept, Iterable<HRResourceWritable> hrResources, Context context) throws IOException, InterruptedException {
		int resignationCount = 0;
		for (HRResourceWritable hrResource : hrResources) {
			if(hrResource.getHasResigned().get()) {
				resignationCount++;
			}
		}
		
		context.write(dept, new IntWritable(resignationCount));
	}
	
	
}
