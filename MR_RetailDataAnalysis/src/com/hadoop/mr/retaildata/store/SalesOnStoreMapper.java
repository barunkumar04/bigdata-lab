package com.hadoop.mr.retaildata.store;

import java.io.IOException;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class SalesOnStoreMapper extends Mapper<Object, Text, Text, FloatWritable>{

	@Override
	protected void map(Object key, Text value, Context context)
			throws IOException, InterruptedException {
		String[] token = value.toString().split("\\t");
		
		//2012-01-01	09:00	San Jose	Men's Clothing	214.05	Amex
		context.write(new Text(token[2]), new FloatWritable(Float.valueOf(token[4])));
	}

	
}
