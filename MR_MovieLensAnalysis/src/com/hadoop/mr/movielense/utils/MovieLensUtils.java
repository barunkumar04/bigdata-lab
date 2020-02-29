package com.hadoop.mr.movielense.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieLensUtils {

	public static final String FILE_RATINGS = "ratings.dat";
	public static final String FILE_MOVIES = "movies.dat";
	public static final String FILE_USERS = "users.dat";
	public static final String TOP_N = "topN";
	public static final String MIN_ELIGIBILITY_VIEWS = "minEligibilityView";
	public static final String SPLITTER_PIPE = "\\|";
	public static final String USER_RATINGS_JOINED_FEED_PATH = "/MovieLens/Input/UserAndRatingJoin/";
	public static final String USER_RATINGS_JOINED_FEED_NAME = "part-r-00000";
	public static final String USER_RATINGS_JOINED_FEED = "/MovieLens/Input/UserAndRatingJoin/part-r-00000";
	public static final List<String> genreList = new ArrayList<String>(Arrays.asList("Action", "Adventure", "Animation",
			"Children's", "Comedy", "Crime", "Documentary", "Drama", "Fantasy", "Film-Noir", "Horror", "Musical",
			"Mystery", "Romance", "Sci-Fi", "Thriller", "War", "Western"));

	public static final Map<String, String> ageGroupMap = createAgeGroupMap();

	public static final Map<String, String> professionMap = createProfessionMap();

	
	private static Map<String, String> createAgeGroupMap() {
		Map<String, String> ageGroupMap = new HashMap<String, String>();
		ageGroupMap.put("18", "18-35");
		ageGroupMap.put("25", "18-35");
		ageGroupMap.put("35", "36-50");
		ageGroupMap.put("45", "36-50");
		ageGroupMap.put("50", "50+");
		ageGroupMap.put("56", "50+");
		return ageGroupMap;
	}

	private static Map<String, String> createProfessionMap() {
		Map<String, String> professionMap = new HashMap<String, String>();
		professionMap.put("0","other");
		professionMap.put("1","academic/educator");
		professionMap.put("2","artist");
		professionMap.put("3","clerical/admin");
		professionMap.put("4","college/grad student");
		professionMap.put("5","customer service");
		professionMap.put("6","doctor/health care");
		professionMap.put("7","executive/managerial");
		professionMap.put("8","farmer");
		professionMap.put("9","homemaker");
		professionMap.put("10","K-12 student");
		professionMap.put("11","lawyer");
		professionMap.put("12","programmer");
		professionMap.put("13","retired");
		professionMap.put("14","sales/marketing");
		professionMap.put("15","scientist");
		professionMap.put("16","self-employed");
		professionMap.put("17","technician/engineer");
		professionMap.put("18","tradesman/craftsman");
		professionMap.put("19","unemployed");
		professionMap.put("20","writer");
		return professionMap;
	}

	public static Boolean validateRatingDetails(String userId, String movieId, String rating) {

		Boolean isValidRating = Boolean.TRUE;
		Integer intUserId = Integer.MIN_VALUE;
		Integer intMovieId = Integer.MIN_VALUE;
		Integer intRating = Integer.MIN_VALUE;

		try {
			intUserId = Integer.parseInt(userId);
			intMovieId = Integer.parseInt(movieId);
			intRating = Integer.parseInt(rating);

		} catch (NumberFormatException e) {
			isValidRating = Boolean.FALSE;
		}

		// UserIDs range between 1 and 6040
		if (intUserId < 1 || intUserId > 6040) {
			isValidRating = Boolean.FALSE;
		}

		// MovieIDs range between 1 and 3952
		if (intMovieId < 1 || intMovieId > 3952) {
			isValidRating = Boolean.FALSE;
		}

		// Ratings are made on a 5-star scale (whole-star ratings only)
		if (intRating < 1 || intRating > 5) {
			isValidRating = Boolean.FALSE;
		}

		return isValidRating;
	}

	public static Boolean validateMovieDetails(String movieId, String movieName) {

		Boolean isValidMovie = Boolean.TRUE;
		Integer intMovieId = Integer.MIN_VALUE;

		try {

			intMovieId = Integer.parseInt(movieId);

		} catch (NumberFormatException e) {
			isValidMovie = Boolean.FALSE;
		}

		// MovieIDs range between 1 and 3952
		if (intMovieId < 1 || intMovieId > 3952) {
			isValidMovie = Boolean.FALSE;
		}

		if (movieName.trim().isEmpty()) {
			isValidMovie = Boolean.FALSE;
		}

		return isValidMovie;

	}

	public static Boolean validateUserRatingJoinedDataDetails(String userId, String genre, String rating) {

		Boolean isValidRating = Boolean.TRUE;
		Integer intUserId = Integer.MIN_VALUE;
		Integer intRating = Integer.MIN_VALUE;

		try {
			intUserId = Integer.parseInt(userId);
			intRating = Integer.parseInt(rating);

		} catch (NumberFormatException e) {
			isValidRating = Boolean.FALSE;
		}

		// UserIDs range between 1 and 6040
		if (intUserId < 1 || intUserId > 6040) {
			isValidRating = Boolean.FALSE;
		}

		if (!genreList.contains(genre)) {
			isValidRating = Boolean.FALSE;
		}

		// Ratings are made on a 5-star scale (whole-star ratings only)
		if (intRating < 1 || intRating > 5) {
			isValidRating = Boolean.FALSE;
		}

		return isValidRating;
	}

}
