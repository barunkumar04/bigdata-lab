package com.hadoop.mr.filejoin;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

public class FileJoinMapper extends Mapper<Object, Text, Text, CustomTextWritable>{
	
	private String fileName;
	
	@Override
	protected void setup(Context context) throws IOException, InterruptedException {
		FileSplit fileSplit = (FileSplit) context.getInputSplit();
		fileName = fileSplit.getPath().getName();
	}

	
	@Override
	protected void map(Object key, Text value, Context context)
			throws IOException, InterruptedException {
		
		String[] splits = value.toString().split(",");
		
		if(splits.length != 2) {
			throw new IOException("Invalid input!");
		}
		
		context.write(new Text(splits[0]), new CustomTextWritable(new Text(fileName), new Text(splits[1])));
	}

}
