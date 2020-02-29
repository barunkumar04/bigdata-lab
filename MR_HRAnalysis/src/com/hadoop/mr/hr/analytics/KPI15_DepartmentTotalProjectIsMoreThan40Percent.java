package com.hadoop.mr.hr.analytics;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

import com.hadoop.mr.hr.analytics.mapper.DepartmentTotalProjectIsMoreThan40PercentMapper;
import com.hadoop.mr.hr.analytics.reducer.DepartmentTotalProjectIsMoreThan40PercentReducer;
import com.hadoop.mr.hr.analytics.util.HRUtil;

/**
 * Name of the department where more than 70% employees left the company
 * 
 */

public class KPI15_DepartmentTotalProjectIsMoreThan40Percent {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		
		Configuration conf = new Configuration();
		
		Job job = new Job(conf, "KPI14_SalaryDistributionOfHighlyExperiencedEmployee");
		
		job.setJarByClass(KPI15_DepartmentTotalProjectIsMoreThan40Percent.class);
		job.setMapperClass(DepartmentTotalProjectIsMoreThan40PercentMapper.class);
		job.setReducerClass(DepartmentTotalProjectIsMoreThan40PercentReducer.class);
		job.setCombinerClass(DepartmentTotalProjectIsMoreThan40PercentReducer.class);
		
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		HRUtil.setupInputOutputPath(job, conf, args[1], Boolean.TRUE, args[0]);
			
		System.exit(job.waitForCompletion(true) ? 0 : 1);
		
	}

}
