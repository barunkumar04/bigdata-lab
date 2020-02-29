

package com.hadoop.mr.weblog;


import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import com.hadoop.mr.weblog.mapper.WeblogParseMapper;
import com.hadoop.mr.weblog.mapper.util.WeblogUtils;

public class WeblogParserJob {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		
		if(args.length != 2) {
			System.err.println("Usage: WeblogParserJob <in1> <in2>");
			System.exit(2);
		}
		
		Configuration conf = new Configuration();
		
		Job job = new Job(conf, "WeblogParserJob");
		
		job.setJarByClass(WeblogParserJob.class);
		job.setMapperClass(WeblogParseMapper.class);
		job.setNumReduceTasks(0);
		
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Text.class);
		
		Path inPath = new Path(args[0]);
		Path outPath = new Path(args[1]);

		FileInputFormat.addInputPath(job, inPath);
		FileOutputFormat.setOutputPath(job, outPath);
		
		MultipleOutputs.addNamedOutput(job, WeblogUtils.MULTI_OUT_PARSED_REC, TextOutputFormat.class, NullWritable.class, Text.class);
		MultipleOutputs.addNamedOutput(job, WeblogUtils.MULTI_OUT_INVALID_REC, TextOutputFormat.class, NullWritable.class, Text.class);
		
		
		FileSystem dfs = FileSystem.get(outPath.toUri(),conf);
		
		if(dfs.exists(outPath))
			dfs.delete(outPath,true);	
		
		System.exit(job.waitForCompletion(true) ? 0 : 1);
		
	}

}
