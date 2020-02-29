package com.hadoop.mr.retaildata.grosscal;

import java.io.IOException;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class GrossSaleAndCountMapper extends Mapper<Object, Text, Text, FloatWritable> {

	@Override
	protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		String[] token = value.toString().split("\\t");

		// 2012-01-01 09:00 San Jose Men's Clothing 214.05 Amex
		context.write(new Text("(Gross Sale, Count)"), new FloatWritable(Float.valueOf(token[4])));
	}

}
