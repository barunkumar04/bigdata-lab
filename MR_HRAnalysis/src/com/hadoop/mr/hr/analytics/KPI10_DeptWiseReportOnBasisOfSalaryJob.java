package com.hadoop.mr.hr.analytics;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

import com.hadoop.mr.hr.analytics.mapper.DeptWiseReportOnBasisOfSalaryMapper;
import com.hadoop.mr.hr.analytics.partitioner.SalaryWisePartitioner;
import com.hadoop.mr.hr.analytics.reducer.DeptWiseReportOnBasisOfSalaryReducer;
import com.hadoop.mr.hr.analytics.util.HRUtil;
import com.hadoop.mr.hr.analytics.writable.HRResourceWritable;

/**
 * For individual department find out the average satisfaction_level, average evaluation and
 * percentage of employees left company on the basis of salary.
 * 
 */

public class KPI10_DeptWiseReportOnBasisOfSalaryJob {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		
		Configuration conf = new Configuration();
		
		Job job = new Job(conf, "KPI10_DeptWiseReportOnBasisOfSalaryJob");
		
		job.setJarByClass(KPI10_DeptWiseReportOnBasisOfSalaryJob.class);
		job.setMapperClass(DeptWiseReportOnBasisOfSalaryMapper.class);
		job.setReducerClass(DeptWiseReportOnBasisOfSalaryReducer.class);
		job.setPartitionerClass(SalaryWisePartitioner.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(HRResourceWritable.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setNumReduceTasks(3);
		
		HRUtil.setupInputOutputPath(job, conf, args[1], Boolean.TRUE, args[0]);
			
		System.exit(job.waitForCompletion(true) ? 0 : 1);
		
	}

}
