package com.hadoop.mr.hr.analytics;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

import com.hadoop.mr.hr.analytics.mapper.BasedOnSalaryCategoryDeptWiseReportMapper;
import com.hadoop.mr.hr.analytics.reducer.BasedOnSalaryCategoryDeptWiseReportReducer;
import com.hadoop.mr.hr.analytics.util.HRUtil;
import com.hadoop.mr.hr.analytics.writable.HRResourceWritable;

//When salary is low find out the Department wise mean satisfaction level , average working hours and no of employee who left company
public class KPI08_DeptWiseReportJobWhenSalaryIsLow {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		
		Configuration conf = new Configuration();
		
		Job job = new Job(conf, "KPI8_BasedOnLowSalaryCategoryDeptWiseReportJob");
		
		job.setJarByClass(KPI08_DeptWiseReportJobWhenSalaryIsLow.class);
		job.setMapperClass(BasedOnSalaryCategoryDeptWiseReportMapper.class);
		job.setReducerClass(BasedOnSalaryCategoryDeptWiseReportReducer.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(HRResourceWritable.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setNumReduceTasks(3);
		
		HRUtil.setupInputOutputPath(job, conf, args[1], Boolean.TRUE, args[0]);
			
		System.exit(job.waitForCompletion(true) ? 0 : 1);
		
	}

}
