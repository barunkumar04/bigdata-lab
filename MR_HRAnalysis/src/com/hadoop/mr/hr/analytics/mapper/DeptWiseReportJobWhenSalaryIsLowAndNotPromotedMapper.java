package com.hadoop.mr.hr.analytics.mapper;

import java.io.IOException;

import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.hadoop.mr.hr.analytics.writable.HRResourceWritable;

public class DeptWiseReportJobWhenSalaryIsLowAndNotPromotedMapper extends Mapper<Object, Text, Text, HRResourceWritable>{

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
		 * dept                 -> sales
		 * salary               -> low
		 * 
		 */
		
		String splits[] = value.toString().split(",");
		
		Text dept = new Text(splits[8]);
		
		Boolean hasResigned = Boolean.FALSE;
		
		FloatWritable satisfactionLevel =  new FloatWritable(Float.valueOf(splits[0]));
		FloatWritable evaluation=  new FloatWritable(Float.valueOf(splits[1]));
		Text salaryCategory = new Text(splits[9]);
		
		if("1".equals(splits[6])) {
			hasResigned = Boolean.TRUE;
		}
		
		String promoted = splits[7];
		
		HRResourceWritable hrResourceWritable = new HRResourceWritable();
		
		hrResourceWritable.setSatisfactionLevel(satisfactionLevel);
		hrResourceWritable.setEvaluation(evaluation);
		hrResourceWritable.setHasResigned(new BooleanWritable(hasResigned));
		hrResourceWritable.setSalaryCategory(salaryCategory);
		hrResourceWritable.setHasPromoted(promoted.equals("1") ? new BooleanWritable(true) : new BooleanWritable(false));
		
		context.write(dept, hrResourceWritable);
		
	}

}
