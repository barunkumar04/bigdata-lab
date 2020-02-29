package com.hadoop.mr.hr.analytics;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

import com.hadoop.mr.hr.analytics.mapper.DeptWhere70PercentEmployeeResignedMapper;
import com.hadoop.mr.hr.analytics.reducer.DeptWhere70PercentEmployeeResignedReducer;
import com.hadoop.mr.hr.analytics.util.HRUtil;
import com.hadoop.mr.hr.analytics.writable.HRResourceWritable;

/**
 * Name of the department where more than 70% employees left the company
 * 
 */

public class KPI12_DeptWhere70PercentEmployeeResigned {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		
		Configuration conf = new Configuration();
		
		Job job = new Job(conf, "KPI12_DeptWhere70PercentEmployeeResigned");
		
		job.setJarByClass(KPI12_DeptWhere70PercentEmployeeResigned.class);
		job.setMapperClass(DeptWhere70PercentEmployeeResignedMapper.class);
		job.setReducerClass(DeptWhere70PercentEmployeeResignedReducer.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(HRResourceWritable.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(DoubleWritable.class);
		
		HRUtil.setupInputOutputPath(job, conf, args[1], Boolean.TRUE, args[0]);
			
		System.exit(job.waitForCompletion(true) ? 0 : 1);
		
	}

}
