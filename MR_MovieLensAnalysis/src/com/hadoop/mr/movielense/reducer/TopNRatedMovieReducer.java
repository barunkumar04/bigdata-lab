package com.hadoop.mr.movielense.reducer;

import java.io.IOException;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import com.hadoop.mr.movielense.customizations.MovieDetailsTextWritable;
import com.hadoop.mr.movielense.utils.MovieLensUtils;

public class TopNRatedMovieReducer extends Reducer<Text, MovieDetailsTextWritable, FloatWritable, Text> {

	TreeMap<Float, Text> topNRatedMovies = null;

	String strTopN = "10";
	Integer topN = 10;

	String strMinEligibilityViewCount = "40";
	Integer minEligibilityViewCount = 40;

	
	@Override
	protected void setup(Context context) throws IOException, InterruptedException {
		topNRatedMovies = new TreeMap<Float, Text>(Collections.reverseOrder());
		
		strTopN = context.getConfiguration().get(MovieLensUtils.TOP_N, strTopN);
		topN = Integer.parseInt(strTopN);
		
		strMinEligibilityViewCount = context.getConfiguration().get(MovieLensUtils.MIN_ELIGIBILITY_VIEWS, strMinEligibilityViewCount);
		minEligibilityViewCount = Integer.parseInt(strMinEligibilityViewCount);

	}

	@Override
	protected void cleanup(Context context) throws IOException, InterruptedException {

		for (Entry<Float, Text> entry : topNRatedMovies.entrySet()) {
			Float avgRating = entry.getKey();
			Text movieName = entry.getValue();
			context.write(new FloatWritable(avgRating), new Text(movieName));
		}
	}

	@Override
	protected void reduce(Text movieId, Iterable<MovieDetailsTextWritable> movieAndViewDetails, Context context)
			throws IOException, InterruptedException {
		Integer count = 0;
		Float ratingSum = 0f;
		String movieName = null;

		for (MovieDetailsTextWritable movieDetailsTextWritable : movieAndViewDetails) {

			if (!movieDetailsTextWritable.getIsValid().get()) {
				continue;
			}

			if (movieDetailsTextWritable.getFileName().toString().equals(MovieLensUtils.FILE_RATINGS)) {
				count++;
				ratingSum = ratingSum + movieDetailsTextWritable.getRating().get();
			}

			if (movieDetailsTextWritable.getFileName().toString().equals(MovieLensUtils.FILE_MOVIES)) {
				movieName = movieDetailsTextWritable.getMovieName().toString();
			}
		}

		if(count >= minEligibilityViewCount) {
			float avgRating = ratingSum / count;
			topNRatedMovies.put(avgRating, new Text(movieName));

			if (topNRatedMovies.size() > topN) {
				topNRatedMovies.remove(topNRatedMovies.lastKey());
			}

		}
		
		
	}
}
