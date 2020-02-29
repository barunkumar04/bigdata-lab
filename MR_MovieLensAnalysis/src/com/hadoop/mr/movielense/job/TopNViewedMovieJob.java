package com.hadoop.mr.movielense.job;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.hadoop.mr.movielense.customizations.MovieDetailsTextWritable;
import com.hadoop.mr.movielense.mapper.TopNViewedMovieMapper;
import com.hadoop.mr.movielense.reducer.TopNViewedMovieReducer;
import com.hadoop.mr.movielense.utils.MovieLensUtils;

public class TopNViewedMovieJob {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

		if (args.length != 4) {
			System.err.println("Usage: TopNViewedMovieJob <topN> <in1> <in2> <out>");
			System.exit(2);
		}

		Configuration conf = new Configuration();

		conf.set(MovieLensUtils.TOP_N, args[0]);

		Job job = new Job(conf, "TopNViewedMovieJob");

		job.setJarByClass(TopNViewedMovieJob.class);
		job.setMapperClass(TopNViewedMovieMapper.class);
		job.setReducerClass(TopNViewedMovieReducer.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(MovieDetailsTextWritable.class);

		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(Text.class);

		setupInputOutputPath(job, conf, args[3], args[1], args[2] );

		System.exit(job.waitForCompletion(true) ? 0 : 1);

	}

	private static void setupInputOutputPath(Job job, Configuration conf, String strOutPath, String... strInPaths) throws IllegalArgumentException, IOException {
		
		for (String inPath : strInPaths) {
			FileInputFormat.addInputPath(job, new Path(inPath));
		}
		
		Path outPath = new Path(strOutPath);
		FileOutputFormat.setOutputPath(job, outPath);
		FileSystem dfs = FileSystem.get(outPath.toUri(), conf);
		
		if (dfs.exists(outPath)) 
			dfs.delete(outPath, true);
		 
	}

}
