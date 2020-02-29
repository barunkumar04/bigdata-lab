package com.hadooop.mr.retaildata.category;

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

public class SalesOnProductCategoryJob {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

		if(args.length != 2) {
			System.err.println("Usages: SalesOnProductCategoryJob <input_path> <output_path>");
		}
		
		Configuration conf = new Configuration();
		Job job = new Job(conf, "SalesOnProductCategoryJob");
		
		job.setJarByClass(SalesOnProductCategoryJob.class);
		job.setMapperClass(SalesOnProductCategoryMapper.class);
		job.setCombinerClass(SalesOnProductCategoryReducer.class);
		job.setReducerClass(SalesOnProductCategoryReducer.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(FloatWritable.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}
