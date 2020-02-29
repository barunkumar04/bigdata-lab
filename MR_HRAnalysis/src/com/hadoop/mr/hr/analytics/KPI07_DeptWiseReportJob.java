package com.hadoop.mr.hr.analytics;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.hadoop.mr.hr.analytics.mapper.DeptWisePromotedButResignedMapper;
import com.hadoop.mr.hr.analytics.mapper.DeptWiseReportMapper;
import com.hadoop.mr.hr.analytics.reducer.DeptWisePromotedButResignedReducer;
import com.hadoop.mr.hr.analytics.reducer.DeptWiseReportReducer;
import com.hadoop.mr.hr.analytics.util.HRUtil;
import com.hadoop.mr.hr.analytics.writable.HRResourceWritable;

//Department wise average satisfaction level, average working hours and no of employee who left company.
public class KPI07_DeptWiseReportJob {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		
		Configuration conf = new Configuration();
		
		Job job = new Job(conf, "KPI7_DeptWiseReportJob");
		
		job.setJarByClass(KPI07_DeptWiseReportJob.class);
		job.setMapperClass(DeptWiseReportMapper.class);
		job.setReducerClass(DeptWiseReportReducer.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(HRResourceWritable.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		HRUtil.setupInputOutputPath(job, conf, args[1], Boolean.TRUE, args[0]);
		
		System.exit(job.waitForCompletion(true) ? 0 : 1);
		
	}

}
