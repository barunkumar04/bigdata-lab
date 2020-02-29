package com.hadoop.mr.movielense.reducer;

import java.io.IOException;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import com.hadoop.mr.movielense.customizations.MovieDetailsTextWritable;
import com.hadoop.mr.movielense.utils.MovieLensUtils;

public class TopNViewedMovieReducer extends Reducer<Text, MovieDetailsTextWritable, IntWritable, Text> {

	TreeMap<Integer, Text> topNViewMovies = null;

	String strTopN = "10";
	Integer topN = 10;

	@Override
	protected void setup(Context context) throws IOException, InterruptedException {
		topNViewMovies = new TreeMap<Integer, Text>(Collections.reverseOrder());
		strTopN = context.getConfiguration().get("topN", strTopN);

		topN = Integer.parseInt(strTopN);

	}

	@Override
	protected void cleanup(Context context) throws IOException, InterruptedException {

		for (Entry<Integer, Text> entry : topNViewMovies.entrySet()) {
			Integer count = entry.getKey();
			Text movieName = entry.getValue();
			context.write(new IntWritable(count), new Text(movieName));
		}
	}

	@Override
	protected void reduce(Text movieId, Iterable<MovieDetailsTextWritable> movieAndViewDetails, Context context)
			throws IOException, InterruptedException {
		Integer count = 0;
		String movieName = null;

		for (MovieDetailsTextWritable movieDetailsTextWritable : movieAndViewDetails) {

			if (!movieDetailsTextWritable.getIsValid().get()) {
				continue;
			}

			if (movieDetailsTextWritable.getFileName().toString().equals(MovieLensUtils.FILE_RATINGS)) {
				count++;
			}

			if (movieDetailsTextWritable.getFileName().toString().equals(MovieLensUtils.FILE_MOVIES)) {
				movieName = movieDetailsTextWritable.getMovieName().toString();
			}
		}

		topNViewMovies.put(count, new Text(movieName));

		if (topNViewMovies.size() > topN) {
			topNViewMovies.remove(topNViewMovies.lastKey());
		}

	}
}
