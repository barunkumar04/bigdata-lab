package com.hadoop.mr.hr.analytics;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

import com.hadoop.mr.hr.analytics.mapper.DeptWiseAvgSatisfactionLevelMapper;
import com.hadoop.mr.hr.analytics.reducer.DeptWiseAvgSatisfactionLevelReducer;
import com.hadoop.mr.hr.analytics.util.HRUtil;

//Average satisfaction_level for individual Department.
public class KPI01_DeptWiseAvgSatisfactionLevelJob {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		
		Configuration conf = new Configuration();
		
		Job job = new Job(conf, "KPI1_DeptWiseAvgSatisfactionLevelJob");
		
		job.setJarByClass(KPI01_DeptWiseAvgSatisfactionLevelJob.class);
		job.setMapperClass(DeptWiseAvgSatisfactionLevelMapper.class);
		job.setReducerClass(DeptWiseAvgSatisfactionLevelReducer.class);
		job.setCombinerClass(DeptWiseAvgSatisfactionLevelReducer.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(FloatWritable.class);
		
		HRUtil.setupInputOutputPath(job, conf, args[1], Boolean.TRUE, args[0]);
		
		System.exit(job.waitForCompletion(true) ? 0 : 1);
		
	}

}
