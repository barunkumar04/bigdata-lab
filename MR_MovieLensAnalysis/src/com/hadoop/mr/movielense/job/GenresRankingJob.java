package com.hadoop.mr.movielense.job;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.hadoop.mr.movielense.customizations.MovieDetailsTextWritable;
import com.hadoop.mr.movielense.mapper.GenreRankingGeneratorMapper;
import com.hadoop.mr.movielense.mapper.UsersAndRatingsJoinerMapper;
import com.hadoop.mr.movielense.reducer.GenreRankingGeneratorReducer;
import com.hadoop.mr.movielense.reducer.UsersAndRatingsJoinerReducer;
import com.hadoop.mr.movielense.utils.MovieLensUtils;

public class GenresRankingJob {

	private static final String JOB_USER_AND_RATING_JOINER = "usersAndRatingsJoinerJob";
	private static final String JOB_GENRE_RANKING_GEN_JOINER = "genreRankingGeneratorJob";
	private static int RES_SUCCESS = 0;
	private static int RES_FAIL = 1;

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

		if (args.length != 4) {
			System.err.println("Usage: GenresRankingJob <in1> <in2> <in3> <out>");
			System.exit(2);
		}

		int result = userAndRatingJoinerJob(args, MovieLensUtils.USER_RATINGS_JOINED_FEED_PATH);

		if (result == RES_SUCCESS) {
			result = genreRankingGeneratorJob(MovieLensUtils.USER_RATINGS_JOINED_FEED_PATH+MovieLensUtils.USER_RATINGS_JOINED_FEED_NAME, args[2], args[3]);
		} else {
			System.err.println(JOB_USER_AND_RATING_JOINER + " has failed!");
			System.exit(RES_FAIL);
		}

	}

	private static int genreRankingGeneratorJob(String userAndRatingJoinedFilePath, String userFilePath, String outPath)
			throws ClassNotFoundException, IOException, InterruptedException {

		Configuration conf = new Configuration();

		Job genreRankingGeneratorJob = new Job(conf, JOB_GENRE_RANKING_GEN_JOINER);

		genreRankingGeneratorJob.setJarByClass(GenresRankingJob.class);
		genreRankingGeneratorJob.setMapperClass(GenreRankingGeneratorMapper.class);
		genreRankingGeneratorJob.setReducerClass(GenreRankingGeneratorReducer.class);

		genreRankingGeneratorJob.setMapOutputKeyClass(Text.class);
		genreRankingGeneratorJob.setMapOutputValueClass(MovieDetailsTextWritable.class);

		genreRankingGeneratorJob.setOutputKeyClass(NullWritable.class);
		genreRankingGeneratorJob.setOutputValueClass(Text.class);

		setupInputOutputPath(genreRankingGeneratorJob, conf, outPath, Boolean.TRUE, userAndRatingJoinedFilePath,
				userFilePath);

		return genreRankingGeneratorJob.waitForCompletion(true) ? RES_SUCCESS : RES_FAIL;

	}

	private static int userAndRatingJoinerJob(String[] args, String jobUserAndRatingJoinerOutPath)
			throws IllegalArgumentException, IOException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();

		Job usersAndRatingsJoinerJob = new Job(conf, JOB_USER_AND_RATING_JOINER);

		usersAndRatingsJoinerJob.setJarByClass(GenresRankingJob.class);
		usersAndRatingsJoinerJob.setMapperClass(UsersAndRatingsJoinerMapper.class);
		usersAndRatingsJoinerJob.setReducerClass(UsersAndRatingsJoinerReducer.class);

		usersAndRatingsJoinerJob.setMapOutputKeyClass(Text.class);
		usersAndRatingsJoinerJob.setMapOutputValueClass(MovieDetailsTextWritable.class);

		usersAndRatingsJoinerJob.setOutputKeyClass(NullWritable.class);
		usersAndRatingsJoinerJob.setOutputValueClass(Text.class);

		setupInputOutputPath(usersAndRatingsJoinerJob, conf, jobUserAndRatingJoinerOutPath, Boolean.TRUE, args[0],
				args[1], args[2]);

		return usersAndRatingsJoinerJob.waitForCompletion(true) ? RES_SUCCESS : RES_FAIL;

	}

	private static void setupInputOutputPath(Job job, Configuration conf, String strOutPath, Boolean deleteOutPath,
			String... strInPaths) throws IllegalArgumentException, IOException {

		for (String inPath : strInPaths) {
			FileInputFormat.addInputPath(job, new Path(inPath));
			System.err.print("Setting in file path: "+inPath);
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
