package com.hadoop.mr.hr.analytics;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

import com.hadoop.mr.hr.analytics.mapper.DeptWiseReportJobWhenSalaryIsLowAndNotPromotedMapper;
import com.hadoop.mr.hr.analytics.partitioner.SalaryWisePartitioner;
import com.hadoop.mr.hr.analytics.reducer.DeptWiseReportJobWhenSalaryIsLowAndNotPromotedReducer;
import com.hadoop.mr.hr.analytics.util.HRUtil;
import com.hadoop.mr.hr.analytics.writable.HRResourceWritable;

/**
 * When salary is low and not promoted in last 5 year than find out the Department wise mean
 * satisfaction level, average working hours and no of employee who left company.
 * 
 */

public class KPI09_DeptWiseReportJobWhenSalaryIsLowAndNotPromoted {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		
		Configuration conf = new Configuration();
		
		Job job = new Job(conf, "KPI9_DeptWiseReportJobWhenSalaryIsLowAndNotPromoted");
		
		job.setJarByClass(KPI09_DeptWiseReportJobWhenSalaryIsLowAndNotPromoted.class);
		job.setMapperClass(DeptWiseReportJobWhenSalaryIsLowAndNotPromotedMapper.class);
		job.setReducerClass(DeptWiseReportJobWhenSalaryIsLowAndNotPromotedReducer.class);
		job.setPartitionerClass(SalaryWisePartitioner.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(HRResourceWritable.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setNumReduceTasks(3);
		
		HRUtil.setupInputOutputPath(job, conf, args[1], Boolean.TRUE, args[0]);
			
		System.exit(job.waitForCompletion(true) ? 0 : 1);
		
	}

}
