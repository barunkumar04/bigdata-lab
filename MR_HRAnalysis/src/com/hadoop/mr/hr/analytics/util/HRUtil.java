package com.hadoop.mr.hr.analytics.util;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class HRUtil {
	public static final String SAL_LOW = "low";

	public static void setupInputOutputPath(Job job, Configuration conf, String strOutPath, Boolean deleteOutPath,
			String... strInPaths) throws IllegalArgumentException, IOException {

		for (String inPath : strInPaths) {
			FileInputFormat.addInputPath(job, new Path(inPath));
			System.err.print("Setting in file path: " + inPath);
		}

		Path outPath = new Path(strOutPath);
		FileOutputFormat.setOutputPath(job, outPath);
		FileSystem dfs = FileSystem.get(outPath.toUri(), conf);
		if (deleteOutPath) {
			if (dfs.exists(outPath))
				dfs.delete(outPath, true);
		}
	}

}
