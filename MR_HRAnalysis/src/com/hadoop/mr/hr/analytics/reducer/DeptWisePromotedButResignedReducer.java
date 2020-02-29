package com.hadoop.mr.hr.analytics.reducer;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import com.hadoop.mr.hr.analytics.writable.HRResourceWritable;

public class DeptWisePromotedButResignedReducer extends Reducer<Text, HRResourceWritable, Text, IntWritable> {

	@Override
	protected void reduce(Text dept, Iterable<HRResourceWritable> promtionsResignations, Context context) throws IOException, InterruptedException {
		Integer promotedButResignedCount = Integer.valueOf(0);

		for (HRResourceWritable promtionsResignation : promtionsResignations) {
			if(promtionsResignation.getHasPromoted().get() && promtionsResignation.getHasResigned().get()) {
				promotedButResignedCount++;
			}
		}
		
		context.write(dept, new IntWritable(promotedButResignedCount));
	}
	
	
}
