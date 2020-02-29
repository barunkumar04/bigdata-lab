package com.hadoop.mr.movielense.mapper;

import java.io.IOException;

import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import com.hadoop.mr.movielense.customizations.MovieDetailsTextWritable;
import com.hadoop.mr.movielense.utils.MovieLensUtils;

public class TopNViewedMovieMapper extends Mapper<Object, Text, Text, MovieDetailsTextWritable> {

	private String fileName;

	@Override
	protected void setup(Context context) throws IOException, InterruptedException {
		FileSplit fileSplit = (FileSplit) context.getInputSplit();
		fileName = fileSplit.getPath().getName();
	}

	@Override
	protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		/**
		 * -- ratings.dat: 
		 * 		- Format: UserID::MovieID::Rating::Timestamp 
		 *      - Values: 1::1193::5::978300760 
		 * -- movies.dat 
		 *      - Format: MovieID::Title::Genres 
		 *      - Values: 1193::One Flew Over the Cuckoo's Nest (1975)::Drama
		 * 
		 */

		String splits[] = value.toString().split("::");

		Boolean isValid = Boolean.TRUE;

		if (fileName.contains(MovieLensUtils.FILE_RATINGS)) {
			String userId = splits[0];
			String movieId = splits[1];
			String rating = splits[2];

			isValid = MovieLensUtils.validateRatingDetails(userId, movieId, rating);

			MovieDetailsTextWritable movieDetails = new MovieDetailsTextWritable();

			movieDetails.setFileName(new Text(fileName));
			movieDetails.setUserID(new Text(userId));
			movieDetails.setIsValid(new BooleanWritable(isValid));

			context.write(new Text(movieId), movieDetails);
		}

		if (fileName.contains(MovieLensUtils.FILE_MOVIES)) {
			String movieId = splits[0];
			String movieName = splits[1];
			isValid = MovieLensUtils.validateMovieDetails(movieId, movieName);

			MovieDetailsTextWritable movieDetails = new MovieDetailsTextWritable();

			movieDetails.setFileName(new Text(fileName));
			movieDetails.setMovieName(new Text(movieName));
			movieDetails.setIsValid(new BooleanWritable(isValid));

			context.write(new Text(movieId), movieDetails);
		}

	}

}