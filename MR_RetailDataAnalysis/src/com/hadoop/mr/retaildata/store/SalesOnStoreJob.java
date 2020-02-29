package com.hadoop.mr.retaildata.store;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class SalesOnStoreJob {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

		if(args.length != 2) {
			System.err.println("Usages: SalesOnProductCategoryJob <input_path> <output_path>");
		}
		
		Configuration conf = new Configuration();
		Job job = new Job(conf, "SalesOnStoreJob");
		
		job.setJarByClass(SalesOnStoreJob.class);
		job.setMapperClass(SalesOnStoreMapper.class);
		job.setCombinerClass(SalesOnStoreReducer.class);
		job.setReducerClass(SalesOnStoreReducer.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(FloatWritable.class);

		MultipleOutputs.addNamedOutput(job, "BadRecords", TextOutputFormat.class, Text.class, FloatWritable.class);
		MultipleOutputs.addNamedOutput(job, "StoreLevelSalesData", TextOutputFormat.class, Text.class, FloatWritable.class);
		
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}
