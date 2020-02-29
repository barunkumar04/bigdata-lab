package com.hadoop.mr.hr.analytics.reducer;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class DeptWiseProjectDoneReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

	@Override
	protected void reduce(Text dept, Iterable<IntWritable> projectsDone, Context context) throws IOException, InterruptedException {
		Integer sumProjectsDone = Integer.valueOf(0);
		
		for (IntWritable projectDone : projectsDone) {
			sumProjectsDone = sumProjectsDone + projectDone.get();
		}
		
		context.write(dept, new IntWritable(sumProjectsDone));
	}
	
	
}
