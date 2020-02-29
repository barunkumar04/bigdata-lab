package com.hadoop.mr.sarloganalysis;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class MemoryUtilizationReducer extends Reducer<Text, FloatWritable, Text, FloatWritable> {

	public void reduce(Text hostnameAndDate, Iterable<FloatWritable> percentageUtilizations, Context context)
			throws IOException, InterruptedException {
		
		Float sum = new Float(0);
		int count = 0;
		Iterator<FloatWritable> itr = percentageUtilizations.iterator();
		
		while (itr.hasNext()) {
			sum = sum + itr.next().get();
			count = count+ 1;
		}
		context.write(hostnameAndDate, new FloatWritable(sum/count));
	}

}
