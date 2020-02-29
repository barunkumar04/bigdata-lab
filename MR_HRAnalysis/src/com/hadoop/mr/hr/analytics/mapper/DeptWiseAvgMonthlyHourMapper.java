package com.hadoop.mr.hr.analytics.mapper;

import java.io.IOException;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class DeptWiseAvgMonthlyHourMapper extends Mapper<Object, Text, Text, FloatWritable>{

	@Override
	protected void map(Object key, Text value, Context context)
			throws IOException, InterruptedException {
		
		/*
		 * satisfaction_level   -> 0.38
		 * last_evaluation      -> 0.53
		 * number_project       -> 2
		 * average_montly_hours -> 157 
		 * time_spend_company   -> 3
		 * Work_accident        -> 0
		 * left                 -> 1 
		 * promotion_last_5years-> 0
		 * dept                -> sales
		 * salary               -> low
		 * 
		 */
		
		String splits[] = value.toString().split(",");
		
		Text dept = new Text(splits[8]);
		FloatWritable averageMontlyHours = new FloatWritable(Float.valueOf(splits[3]));
		
		context.write(dept, averageMontlyHours);
		
	}

}
