package com.hadoop.mr.hr.analytics.partitioner;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

import com.hadoop.mr.hr.analytics.writable.HRResourceWritable;

public class SalaryWisePartitioner extends Partitioner<Text, HRResourceWritable>{
	
	private int SALARY_LOW = 0;
	private int SALARY_MEDIUM = 1;
	private int SALARY_HIGH = 2;
	private int SALARY_INVALID = 3;
	
	private String SALARY_CAT_LOW = "low";
	private String SALARY_CAT_MEDIUM = "medium";
	private String SALARY_CAT_HIGH = "high";
	
	@Override
	public int getPartition(Text arg0, HRResourceWritable hrResoWritable, int reducerTaskCount) {
		
		if(reducerTaskCount != 0 ) {
			if(SALARY_CAT_LOW.equalsIgnoreCase(hrResoWritable.getSalaryCategory().toString())) {
				return SALARY_LOW;
			}else if(SALARY_CAT_MEDIUM.equalsIgnoreCase(hrResoWritable.getSalaryCategory().toString())) {
				return SALARY_MEDIUM;
			}else if(SALARY_CAT_HIGH.equalsIgnoreCase(hrResoWritable.getSalaryCategory().toString())) {
				return SALARY_HIGH;
			}else {
				return SALARY_INVALID;
			}
		}
		return 0;
	}

}
