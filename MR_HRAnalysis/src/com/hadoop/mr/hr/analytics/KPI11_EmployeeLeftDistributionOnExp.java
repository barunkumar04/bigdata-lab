package com.hadoop.mr.hr.analytics;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

import com.hadoop.mr.hr.analytics.mapper.EmployeeLeftDistributionOnExpMapper;
import com.hadoop.mr.hr.analytics.partitioner.ExperianceWisePartitioner;
import com.hadoop.mr.hr.analytics.reducer.EmployeeLeftDistributionOnExpReducer;
import com.hadoop.mr.hr.analytics.util.HRUtil;
import com.hadoop.mr.hr.analytics.writable.HRResourceWritable;

/**
 * How many employees left, distribution based on experience.
 * 
 */

public class KPI11_EmployeeLeftDistributionOnExp {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		
		Configuration conf = new Configuration();
		
		Job job = new Job(conf, "KPI11_EmployeeLeftDistributionOnExp");
		
		job.setJarByClass(KPI11_EmployeeLeftDistributionOnExp.class);
		job.setMapperClass(EmployeeLeftDistributionOnExpMapper.class);
		job.setReducerClass(EmployeeLeftDistributionOnExpReducer.class);
		job.setPartitionerClass(ExperianceWisePartitioner.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(HRResourceWritable.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		job.setNumReduceTasks(8);
		
		HRUtil.setupInputOutputPath(job, conf, args[1], Boolean.TRUE, args[0]);
			
		System.exit(job.waitForCompletion(true) ? 0 : 1);
		
	}

}
