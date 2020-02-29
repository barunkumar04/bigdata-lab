package com.hadoop.mr.retaildata.grosscal;

import java.io.IOException;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

public class GrossSaleAndCountReducer extends Reducer<Text, FloatWritable, Text, Text>{

	MultipleOutputs<Text, FloatWritable> multipleOutputWriter= null;
	

	@Override
	protected void reduce(Text key, Iterable<FloatWritable> prices, Context context) throws IOException, InterruptedException {
		
		Float priceSum = Float.valueOf(0);
		Integer count = 0;
		for (FloatWritable floatWritable : prices) {
			priceSum = priceSum + floatWritable.get();
			count++;
		}
		
		context.write(key, new Text("("+String.valueOf(priceSum)+","+count+")"));
	}

	
	
}
