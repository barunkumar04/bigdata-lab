package com.hadoop.mr.movielense.reducer;

import java.io.IOException;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import com.hadoop.mr.movielense.customizations.MovieDetailsTextWritable;
import com.hadoop.mr.movielense.utils.MovieLensUtils;

public class UsersAndRatingsJoinerReducer extends Reducer<Text, MovieDetailsTextWritable, NullWritable, Text> {

	TreeMap<Float, Text> topNRatedMovies = null;

	String strTopN = "10";
	Integer topN = 10;

	String strMinEligibilityViewCount = "40";
	Integer minEligibilityViewCount = 40;

	@Override
	protected void reduce(Text movieId, Iterable<MovieDetailsTextWritable> movieAndViewDetails, Context context)
			throws IOException, InterruptedException {
		String movieName = null;
		String movieGenres = null;

		for (MovieDetailsTextWritable movieDetailsTextWritable : movieAndViewDetails) {

			if (!movieDetailsTextWritable.getIsValid().get()) {
				continue;
			}

			if (movieDetailsTextWritable.getFileName().toString().equals(MovieLensUtils.FILE_MOVIES)) {
				movieName = movieDetailsTextWritable.getMovieName().toString();
				movieGenres = movieDetailsTextWritable.getGenres().toString();
				break;
			}
		}

		String[] movieGenresSplit = movieGenres.split(MovieLensUtils.SPLITTER_PIPE);

		for (MovieDetailsTextWritable movieDetailsTextWritable : movieAndViewDetails) {

			if (!movieDetailsTextWritable.getIsValid().get()) {
				continue;
			}

			if (movieDetailsTextWritable.getFileName().toString().equals(MovieLensUtils.FILE_RATINGS)) {
				String userId = movieDetailsTextWritable.getUserID().toString();
				String movieRating = movieDetailsTextWritable.getRating().toString();

				// UserID::MovieName::Genres::Rating

				for (String genresSplit : movieGenresSplit) {
					StringBuilder sb = new StringBuilder();
					sb = sb.append(userId).append("::").append(movieName).append("::").append(genresSplit).append("::")
							.append(movieRating);
					context.write(NullWritable.get(), new Text(sb.toString()));
				}
			}
		}

	}
}
