package com.hadoop.mr.retaildata.grosscal;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.sun.jersey.core.impl.provider.entity.XMLJAXBElementProvider.Text;

public class GrossSaleAndCountJob {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

		if(args.length != 2) {
			System.err.println("Usages: GrossSaleAndCountJob <input_path> <output_path>");
			System.exit(2);
		}
		
		Configuration conf = new Configuration();
		Job job = new Job(conf,"GrossSaleAndCountJob");
		
		job.setJarByClass(GrossSaleAndCountJob.class);
		job.setMapperClass(GrossSaleAndCountMapper.class);
		job.setReducerClass(GrossSaleAndCountReducer.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(FloatWritable.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		job.setNumReduceTasks(1);
		
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		
		System.exit(job.waitForCompletion(true) ? 0 : 2);
	}

}
