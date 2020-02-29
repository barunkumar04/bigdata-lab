package com.hadoop.mr.hr.analytics;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

import com.hadoop.mr.hr.analytics.mapper.DeptWisePromotedButResignedMapper;
import com.hadoop.mr.hr.analytics.reducer.DeptWisePromotedButResignedReducer;
import com.hadoop.mr.hr.analytics.util.HRUtil;
import com.hadoop.mr.hr.analytics.writable.HRResourceWritable;

//In individual Department How many Employees promoted in last 5 years but still left the company
public class KPI06_DeptWisePromotedButResignedJob {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		
		Configuration conf = new Configuration();
		
		Job job = new Job(conf, "KPI5_DeptWisePromotedButResignedJob");
		
		job.setJarByClass(KPI06_DeptWisePromotedButResignedJob.class);
		job.setMapperClass(DeptWisePromotedButResignedMapper.class);
		job.setReducerClass(DeptWisePromotedButResignedReducer.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(HRResourceWritable.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		HRUtil.setupInputOutputPath(job, conf, args[1], Boolean.TRUE, args[0]);
		
		
		System.exit(job.waitForCompletion(true) ? 0 : 1);
		
	}

}
