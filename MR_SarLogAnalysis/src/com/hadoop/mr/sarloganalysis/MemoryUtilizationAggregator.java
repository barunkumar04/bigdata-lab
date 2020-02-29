package com.hadoop.mr.sarloganalysis;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class MemoryUtilizationAggregator {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		if (otherArgs.length < 2)
		{
			System.err.println("Usage: MemoryUtilizationAggregator <in> [<in>...] <out>");
			System.exit(2);
		}
		Job job = new Job(conf, "MemoryUtilizationAggregator");
		job.setJarByClass(MemoryUtilizationAggregator.class);
		job.setMapperClass(MemoryUtilizationMapper.class);
		job.setCombinerClass(MemoryUtilizationReducer.class);
		job.setReducerClass(MemoryUtilizationReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(FloatWritable.class);
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	
	}

}
