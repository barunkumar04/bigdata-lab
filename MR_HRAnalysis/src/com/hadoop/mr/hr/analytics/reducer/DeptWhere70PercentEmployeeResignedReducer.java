package com.hadoop.mr.hr.analytics.reducer;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import com.hadoop.mr.hr.analytics.writable.HRResourceWritable;

public class DeptWhere70PercentEmployeeResignedReducer extends Reducer<Text, HRResourceWritable, Text, DoubleWritable> {

	@Override
	protected void reduce(Text dept, Iterable<HRResourceWritable> hrResources, Context context) throws IOException, InterruptedException {
		int count = 0;
		int resignationCount = 0;
		for (HRResourceWritable hrResource : hrResources) {
			if(hrResource.getHasResigned().get()) {
				resignationCount++;
			}
			count++;
		}
		
		double resignationPercentage = (resignationCount * 100.0) / count;
		if(resignationPercentage > 70)
			context.write(dept, new DoubleWritable(resignationPercentage));
	}
	
	
}
