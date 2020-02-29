package com.hadoop.mr.hr.analytics;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

import com.hadoop.mr.hr.analytics.mapper.DeptWiseAvgMonthlyHourMapper;
import com.hadoop.mr.hr.analytics.reducer.DeptWiseAvgMonthlyHourReducer;
import com.hadoop.mr.hr.analytics.util.HRUtil;

//Department wise average monthly working hour
public class KPI03_DeptWiseAvgMonthlyHourJob {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		
		Configuration conf = new Configuration();
		
		Job job = new Job(conf, "DeptWiseAvgSatisfactionLevelJob");
		
		job.setJarByClass(KPI03_DeptWiseAvgMonthlyHourJob.class);
		job.setMapperClass(DeptWiseAvgMonthlyHourMapper.class);
		job.setReducerClass(DeptWiseAvgMonthlyHourReducer.class);
		job.setCombinerClass(DeptWiseAvgMonthlyHourReducer.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(FloatWritable.class);
		
		HRUtil.setupInputOutputPath(job, conf, args[1], Boolean.TRUE, args[0]);
		
		
		System.exit(job.waitForCompletion(true) ? 0 : 1);
		
	}

}
