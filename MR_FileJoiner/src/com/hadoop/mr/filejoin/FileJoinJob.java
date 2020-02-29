package com.hadoop.mr.filejoin;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class FileJoinJob {

	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		Job job = new Job(conf, "FileJoinJob");
		
		if(args.length != 3) {
			System.err.println("Usage: FileJoinJob <in1> <in2> <out>");
			System.exit(2);
		}
		
		job.setJarByClass(FileJoinJob.class);
		job.setMapperClass(FileJoinMapper.class);
		//job.setCombinerClass(FileJoinReducer.class);
		job.setReducerClass(FileJoinReducer.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(CustomTextWritable.class);
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Text.class);
		
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileInputFormat.addInputPath(job, new Path(args[1]));
		
		FileOutputFormat.setOutputPath(job, new Path(args[2]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
		
	}

}
