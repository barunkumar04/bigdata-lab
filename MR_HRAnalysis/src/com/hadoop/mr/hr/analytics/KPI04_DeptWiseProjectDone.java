package com.hadoop.mr.hr.analytics;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

import com.hadoop.mr.hr.analytics.mapper.DeptWiseProjectMapper;
import com.hadoop.mr.hr.analytics.reducer.DeptWiseProjectDoneReducer;
import com.hadoop.mr.hr.analytics.util.HRUtil;

//No of Project done by individual Department.
public class KPI04_DeptWiseProjectDone {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		
		Configuration conf = new Configuration();
		
		Job job = new Job(conf, "KPI4_DeptWiseProjectDone");
		
		job.setJarByClass(KPI04_DeptWiseProjectDone.class);
		job.setMapperClass(DeptWiseProjectMapper.class);
		job.setReducerClass(DeptWiseProjectDoneReducer.class);
		job.setCombinerClass(DeptWiseProjectDoneReducer.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		HRUtil.setupInputOutputPath(job, conf, args[1], Boolean.TRUE, args[0]);
		
		
		System.exit(job.waitForCompletion(true) ? 0 : 1);
		
	}

}
