package com.hadoop.mr.hr.analytics;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

import com.hadoop.mr.hr.analytics.mapper.DepartmentWiseSalaryDistributionMapper;
import com.hadoop.mr.hr.analytics.partitioner.SalaryWisePartitioner;
import com.hadoop.mr.hr.analytics.reducer.DepartmentWiseSalaryDistributionReducer;
import com.hadoop.mr.hr.analytics.util.HRUtil;

//Department wise salary Distribution
public class KPI05_DepartmentWiseSalaryDistribution {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		
		Configuration conf = new Configuration();
		
		Job job = new Job(conf, "KPI5_DepartmentWiseSalaryDistribution");
		
		job.setJarByClass(KPI05_DepartmentWiseSalaryDistribution.class);
		job.setMapperClass(DepartmentWiseSalaryDistributionMapper.class);
		job.setReducerClass(DepartmentWiseSalaryDistributionReducer.class);
		job.setPartitionerClass(SalaryWisePartitioner.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		job.setNumReduceTasks(3);
		
		HRUtil.setupInputOutputPath(job, conf, args[1], Boolean.TRUE, args[0]);
			
		System.exit(job.waitForCompletion(true) ? 0 : 1);
		
	}

}
