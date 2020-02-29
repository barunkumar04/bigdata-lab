package com.hadoop.mr.hr.analytics;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

import com.hadoop.mr.hr.analytics.mapper.DeptWiseEmployeeResignationMapper;
import com.hadoop.mr.hr.analytics.reducer.DeptWiseEmployeeResignationReducer;
import com.hadoop.mr.hr.analytics.util.HRUtil;
//How many employees are left in each individual Department?
public class KPI02_DeptWiseEmployeeResignationJob {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		
		Configuration conf = new Configuration();
		
		Job job = new Job(conf, "KPI2_DeptWiseEmployeeResignationJob");
		
		job.setJarByClass(KPI02_DeptWiseEmployeeResignationJob.class);
		job.setMapperClass(DeptWiseEmployeeResignationMapper.class);
		job.setReducerClass(DeptWiseEmployeeResignationReducer.class);
		job.setCombinerClass(DeptWiseEmployeeResignationReducer.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		HRUtil.setupInputOutputPath(job, conf, args[1], Boolean.TRUE, args[0]);
		
		System.exit(job.waitForCompletion(true) ? 0 : 1);
		
	}

}
