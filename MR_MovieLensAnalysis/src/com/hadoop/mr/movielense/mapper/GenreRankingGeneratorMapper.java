package com.hadoop.mr.movielense.mapper;

import java.io.IOException;

import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import com.hadoop.mr.movielense.customizations.MovieDetailsTextWritable;
import com.hadoop.mr.movielense.utils.MovieLensUtils;

public class GenreRankingGeneratorMapper extends Mapper<Object, Text, Text, MovieDetailsTextWritable>{

	private String fileName;
	
	@Override
	protected void setup(Context context) throws IOException, InterruptedException {
		FileSplit fileSplit = (FileSplit) context.getInputSplit();
		fileName = fileSplit.getPath().getName();
	}
	
	@Override
	protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		/**
		 *  -- /MovieLens/Input/UserAndRatingJoin/part-r-00000 [This file has User and Rating joined data] 
		 *  	- Format: UserID::MovieName::Genre::Rating
		 *  	- Values: 977::Forbidden Planet (1956)::Sci-Fi::4.0
		 *  -- User.dat 
		 *  	- Format: UserID::Gender::Age::Occupation::Zip-code
		 *  	- Values: 1::F::1::10::48067
		 * 
		 */
		
		String splits[] = value.toString().split("::");
		
		Boolean isValid = Boolean.TRUE;
		
		if (fileName.contains(MovieLensUtils.USER_RATINGS_JOINED_FEED_NAME)) {
			
			String userId = splits[0];
			String genre = splits[2];
			String rating = splits[3];

			isValid = MovieLensUtils.validateUserRatingJoinedDataDetails(userId, genre, rating);

			MovieDetailsTextWritable movieDetails = new MovieDetailsTextWritable();

			movieDetails.setFileName(new Text(fileName));
			movieDetails.setUserID(new Text(userId));
			movieDetails.setGenres(new Text(genre));
			movieDetails.setRating(new FloatWritable(Float.valueOf(rating)));
			movieDetails.setIsValid(new BooleanWritable(isValid));
			context.write(new Text(userId), movieDetails);
		}

		if (fileName.contains(MovieLensUtils.FILE_USERS)) {
			String userId = splits[0];
			String ageGroup = splits[2];
			String occupation = splits[3];

			MovieDetailsTextWritable movieDetails = new MovieDetailsTextWritable();

			movieDetails.setFileName(new Text(fileName));
			movieDetails.setUserID(new Text(userId));
			movieDetails.setAgeGroup(new Text(ageGroup));
			movieDetails.setOccupation(new Text(occupation));

			context.write(new Text(userId), movieDetails);
		}

		
	}
	
}