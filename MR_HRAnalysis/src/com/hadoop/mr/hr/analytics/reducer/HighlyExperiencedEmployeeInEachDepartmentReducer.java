package com.hadoop.mr.hr.analytics.reducer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class HighlyExperiencedEmployeeInEachDepartmentReducer extends Reducer<Text, Text, Text, Text> {

	private Map<String, String> mostExperiancedEmployees;
	
	@Override
	protected void setup(Reducer<Text, Text, Text, Text>.Context context)
			throws IOException, InterruptedException {
		
		mostExperiancedEmployees = new HashMap<String, String>();
		
	}
	
	
	
	@Override
	protected void cleanup(Reducer<Text, Text, Text, Text>.Context context)
			throws IOException, InterruptedException {
		for (Entry<String, String> entry : mostExperiancedEmployees.entrySet()) {
			context.write(new Text(entry.getKey()), new Text(entry.getValue()));
		}
	}


	@Override
	protected void reduce(Text dept, Iterable<Text> employees, Context context) throws IOException, InterruptedException {
		
		int maxExp = 0;
		String maxExpEmployee = null;
		
		for (Text employee : employees) {
			int empExp = Integer.valueOf(employee.toString().split(",")[4]);
			if(empExp > maxExp ) {
				maxExp = empExp;
				maxExpEmployee = employee.toString();
			}	
		}
		
		mostExperiancedEmployees.put(dept.toString(), maxExpEmployee);
	}
	
	
}
