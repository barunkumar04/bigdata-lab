package com.hadoop.mr.hr.analytics;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

import com.hadoop.mr.hr.analytics.mapper.DeptWhere70PercentEmployeeResignedMapper;
import com.hadoop.mr.hr.analytics.mapper.HighlyExperiencedEmployeeInEachDepartmentMapper;
import com.hadoop.mr.hr.analytics.reducer.DeptWhere70PercentEmployeeResignedReducer;
import com.hadoop.mr.hr.analytics.reducer.HighlyExperiencedEmployeeInEachDepartmentReducer;
import com.hadoop.mr.hr.analytics.util.HRUtil;

/**
 * Name of the department where more than 70% employees left the company
 * 
 */

public class KPI13_HighlyExperiencedEmployeeInEachDepartment {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		
		Configuration conf = new Configuration();
		
		Job job = new Job(conf, "KPI13_HighlyExperiencedEmployeeInEachDepartment");
		
		job.setJarByClass(KPI13_HighlyExperiencedEmployeeInEachDepartment.class);
		job.setMapperClass(HighlyExperiencedEmployeeInEachDepartmentMapper.class);
		job.setReducerClass(HighlyExperiencedEmployeeInEachDepartmentReducer.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		HRUtil.setupInputOutputPath(job, conf, args[1], Boolean.TRUE, args[0]);
			
		System.exit(job.waitForCompletion(true) ? 0 : 1);
		
	}

}
