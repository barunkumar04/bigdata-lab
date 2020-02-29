package com.hadoop.mr.hr.analytics.reducer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class SalaryDistributionOfHighlyExperiencedEmployeeReducer extends Reducer<Text, Text, NullWritable, Text> {

	private Map<Text, Text> mostExperiancedEmployees;
	
	@Override
	protected void setup(Reducer<Text, Text, NullWritable, Text>.Context context)
			throws IOException, InterruptedException {
		
		mostExperiancedEmployees = new HashMap<Text, Text>();
		
	}
	
	
	
	@Override
	protected void cleanup(Reducer<Text, Text, NullWritable, Text>.Context context)
			throws IOException, InterruptedException {
		for (Entry<Text, Text> entry : mostExperiancedEmployees.entrySet()) {
			context.write(NullWritable.get(), entry.getValue());
		}
	}


	@Override
	protected void reduce(Text dept, Iterable<Text> employees, Context context) throws IOException, InterruptedException {
		
		int maxExp = 0;
		String salaryDistribution = null;
		
		for (Text employee : employees) {
			int exp = Integer.valueOf(employee.toString().split(",")[4]);
			if(exp > maxExp) {
				maxExp = exp;
				salaryDistribution = employee.toString().split(",")[0];
			}	
				
		}
		
		mostExperiancedEmployees.put(dept, new Text(salaryDistribution));
	}
	
	
}
