package com.hadooop.mr.retaildata.category;

import java.io.IOException;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class SalesOnProductCategoryReducer extends Reducer<Text, FloatWritable, Text, FloatWritable>{

	@Override
	protected void reduce(Text category, Iterable<FloatWritable> prices, Context context) throws IOException, InterruptedException {
		
		Float priceSum = Float.valueOf(0);
		
		for (FloatWritable floatWritable : prices) {
			priceSum = priceSum + floatWritable.get();
		}
		
		context.write(category, new FloatWritable(priceSum));
		
	}

	
	
}
