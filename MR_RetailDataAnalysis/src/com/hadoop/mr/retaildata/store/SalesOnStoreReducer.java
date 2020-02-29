package com.hadoop.mr.retaildata.store;

import java.io.IOException;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

public class SalesOnStoreReducer extends Reducer<Text, FloatWritable, Text, FloatWritable>{

	MultipleOutputs<Text, FloatWritable> multipleOutputWriter= null;
	
	
	
	@Override
	protected void setup(Reducer<Text, FloatWritable, Text, FloatWritable>.Context context)
			throws IOException, InterruptedException {
		multipleOutputWriter = new MultipleOutputs<Text, FloatWritable>(context);
		
	}

	


	@Override
	protected void cleanup(Reducer<Text, FloatWritable, Text, FloatWritable>.Context context)
			throws IOException, InterruptedException {
		multipleOutputWriter.close();
	}




	@Override
	protected void reduce(Text storeLocation, Iterable<FloatWritable> prices, Context context) throws IOException, InterruptedException {
		
		Float priceSum = Float.valueOf(0);
		
		for (FloatWritable floatWritable : prices) {
			priceSum = priceSum + floatWritable.get();
		}
		
		/**
		 * Here, I am considering there is no store in Pittsburgh. Hence, any record having store location 
		 * will be directed to bad_records file.
		 */
		if("Pittsburgh".equalsIgnoreCase(storeLocation.toString()))
			multipleOutputWriter.write("BadRecords", storeLocation, new FloatWritable(priceSum));
		else
			multipleOutputWriter.write("StoreLevelSalesData", storeLocation, new FloatWritable(priceSum));
	}

	
	
}
