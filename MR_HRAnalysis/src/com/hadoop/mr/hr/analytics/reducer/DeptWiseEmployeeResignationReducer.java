package com.hadoop.mr.hr.analytics.reducer;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class DeptWiseEmployeeResignationReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

	
	
	@Override
	protected void reduce(Text dept, Iterable<IntWritable> resignations, Context context) throws IOException, InterruptedException {
	
		int resignationCount = 0;

		for (IntWritable resignation : resignations) {
			if(resignation.get() == 1) {
				System.err.println("In Reducer: resignation.get()"+resignation.get());
				resignationCount = resignationCount + 1;
				System.err.println("In Reducer, in loop: sumResignations"+resignationCount);
				
			}	
		}
		System.err.println("In Reducer: sumResignations"+resignationCount);
		
		context.write(dept, new IntWritable(resignationCount));
	}
	
	
}
