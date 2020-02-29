package com.hadoop.mr.hr.analytics.reducer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class DepartmentTotalProjectIsMoreThan40PercentReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

	private Map<String, Integer> deptProjectCount;
	
	@Override
	protected void setup(Reducer<Text, IntWritable, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {
		
		deptProjectCount = new HashMap<String, Integer>();
		
	}
	
	
	
	@Override
	protected void cleanup(Reducer<Text, IntWritable, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {
		
		Integer totalProjectCount = deptProjectCount.values().stream().reduce(0, Integer::sum);
		
		for (Entry<String, Integer> entry : deptProjectCount.entrySet()) {
			double deptProjectCountPercentage = (entry.getValue() * 100.0) / totalProjectCount;
			//if(deptProjectCountPercentage > 40)
				context.write(new Text(entry.getKey()), new IntWritable(entry.getValue()));
		}
	}


	@Override
	protected void reduce(Text dept, Iterable<IntWritable> projectCount, Context context) throws IOException, InterruptedException {
		
		int sumProjectCount = 0;
		
		for (IntWritable count : projectCount) {
			sumProjectCount = sumProjectCount + count.get();
		}
		deptProjectCount.put(dept.toString(), sumProjectCount);
		
	}
	
	
}
