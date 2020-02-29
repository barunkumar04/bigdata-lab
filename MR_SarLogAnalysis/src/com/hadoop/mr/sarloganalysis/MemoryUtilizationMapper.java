package com.hadoop.mr.sarloganalysis;

import java.io.IOException;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MemoryUtilizationMapper extends Mapper <Object, Text, Text, FloatWritable>{
	public void map(Object offsetValue, Text record, Context context) throws IOException, InterruptedException{
		
		// hdtr001 230613,20:50 Average:       473633    319179     40.26     77812     63504    936325     71.31    208009     63161
		String[] recordSplit = record.toString().split("\\s+");
		
		String hostName = recordSplit[0];
		String date = recordSplit[1].split(",")[0];
		Float memUtilPercentage = Float.valueOf(recordSplit[5]);
		
		context.write(new Text(hostName+"-"+date), new FloatWritable(memUtilPercentage));
	}
	
}