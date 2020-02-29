package com.hadoop.mr.movielense.reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.OptionalDouble;
import java.util.TreeMap;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import com.hadoop.mr.movielense.customizations.MovieDetailsTextWritable;
import com.hadoop.mr.movielense.utils.MovieLensUtils;

public class GenreRankingGeneratorReducer extends Reducer<Text, MovieDetailsTextWritable, NullWritable, Text> {

	Map<String, Map<String, List<Float>>> genreRankingRawData = null;
	
	@Override
	protected void setup(Context context) throws IOException, InterruptedException {
		
		genreRankingRawData = new TreeMap<String, Map<String,List<Float>>>();
	}

	@Override
	protected void cleanup(Context context) throws IOException, InterruptedException {
		
		for (Entry<String, Map<String, List<Float>>> genreRankingRaw : genreRankingRawData.entrySet()) {
			
			String tabSeparatedRankedGenres =  getRankedGenres(genreRankingRaw.getValue());
			context.write(NullWritable.get(), new Text(genreRankingRaw.getKey()+"\t"+tabSeparatedRankedGenres));
		}
	}


	private String getRankedGenres(Map<String, List<Float>> genresVsRatingsMap) {
		
		StringBuilder genresSortedOnAvgRanking = new StringBuilder(); 
		
		Map<Float, String> ratingAvgVsGenres = new TreeMap<Float, String>(Collections.reverseOrder());
		
		for (Entry<String, List<Float>> genresVsRatings : genresVsRatingsMap.entrySet()) {
			
			OptionalDouble avgRating = genresVsRatings.getValue().stream()
            .mapToDouble(rating -> rating)
            .average();
			
			ratingAvgVsGenres.put((float) avgRating.getAsDouble(), genresVsRatings.getKey());
		}
		
		for (Entry<Float, String> sortedRatings : ratingAvgVsGenres.entrySet()) {
			genresSortedOnAvgRanking.append(sortedRatings.getValue()).append("\t");
		}
		return genresSortedOnAvgRanking.toString();
	}

	@Override
	protected void reduce(Text movieId, Iterable<MovieDetailsTextWritable> movieAndViewDetails, Context context)
			throws IOException, InterruptedException {
		
		/**
		 *  OCCUPATION	AGE_GROUP	Genre 	Rank1	Rank2 Rank3 ...
		 * 	K-12 student	18-35	Film-Noir	War	Animation	Musical	Drama
		 *	K-12 student	50+	Documentary	Film-Noir	Mystery	Drama	Musical
		 *	executive/managerial	18-35	Film-Noir	Documentary	War	Animation	Drama
		 * 
		 */
		
		String ageGroup = null;
		String profession = null;
		
		for (MovieDetailsTextWritable movieDetailsTextWritable : movieAndViewDetails) {
			if (movieDetailsTextWritable.getFileName().toString().equals(MovieLensUtils.FILE_USERS)) {
				/* 
				 * Key -> UserID
				 * Value -> AgeGroup	profession
				 * 
				 */
				
				ageGroup = MovieLensUtils.ageGroupMap.get(movieDetailsTextWritable.getAgeGroup().toString());
				profession = MovieLensUtils.professionMap.get(movieDetailsTextWritable.getOccupation().toString()); 
				break;
			}
		}	
		
		String rawDataMapKey = profession+"\t"+ageGroup;
		
		Map<String, List<Float>> genreVsRatingsMap = genreRankingRawData.get(rawDataMapKey);
		if(genreVsRatingsMap == null) {
			genreVsRatingsMap = new HashMap<String, List<Float>>();
		}
		
		
		for (MovieDetailsTextWritable movieDetailsTextWritable : movieAndViewDetails) {
			
			if (movieDetailsTextWritable.getFileName().toString().equals(MovieLensUtils.USER_RATINGS_JOINED_FEED_NAME)) {
				/**
				 * Key -> UserID
				 * Value -> Genre	Rating
				 * 
				 */
				
				String genre = movieDetailsTextWritable.getGenres().toString();
				Float rating = movieDetailsTextWritable.getRating().get();
				
				List<Float> ratingList = genreVsRatingsMap.get(genre);
				
				if(ratingList == null) {
					ratingList = new ArrayList<Float>();
				}
				
				ratingList.add(rating);
				
				genreVsRatingsMap.put(genre, ratingList);
				
			}

		}
		if(ageGroup != null) {
			genreRankingRawData.put(rawDataMapKey, genreVsRatingsMap);
		}	
		
	}
}
