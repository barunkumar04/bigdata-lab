package com.hadoop.mr.hr.analytics;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

import com.hadoop.mr.hr.analytics.mapper.SalaryDistributionOfHighlyExperiencedEmployeeMapper;
import com.hadoop.mr.hr.analytics.reducer.SalaryDistributionOfHighlyExperiencedEmployeeReducer;
import com.hadoop.mr.hr.analytics.util.HRUtil;

/**
 * Name of the department where more than 70% employees left the company
 * 
 */

public class KPI14_SalaryDistributionOfHighlyExperiencedEmployee {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		
		Configuration conf = new Configuration();
		
		Job job = new Job(conf, "KPI14_SalaryDistributionOfHighlyExperiencedEmployee");
		
		job.setJarByClass(KPI14_SalaryDistributionOfHighlyExperiencedEmployee.class);
		job.setMapperClass(SalaryDistributionOfHighlyExperiencedEmployeeMapper.class);
		job.setReducerClass(SalaryDistributionOfHighlyExperiencedEmployeeReducer.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Text.class);
		
		HRUtil.setupInputOutputPath(job, conf, args[1], Boolean.TRUE, args[0]);
			
		System.exit(job.waitForCompletion(true) ? 0 : 1);
		
	}

}
