
#############################################################################################
### KPI1: Top ten most viewed movies with their movies Name (Ascending or Descending order) 
#############################################################################################

# Command: yarn jar /home/barun/HadoopWorkshopArtifacts/MovieLens/MovieLensJob.jar com.hadoop.mr.movielense.job.TopNViewedMovieJob 10 /MovieLens/Input/movies.dat /MovieLens/Input/ratings.dat /MovieLens/Output/Top10ViewedMovies/

# Output screenshot: MR_MovieLensAnalysis/output/KPI1_top_n_most_viewed_movies.PNG



#####################################################################################################
### KPI2: Top twenty rated movies (Condition: The movie should be rated/viewed by at least 40 users)
#####################################################################################################

# Command: yarn jar /home/barun/HadoopWorkshopArtifacts/MovieLens/MovieLensJob.jar com.hadoop.mr.movielense.job.TopNRatedMovieJob 20 40 /MovieLens/Input/movies.dat /MovieLens/Input/ratings.dat /MovieLens/Output/Top20RatedMovies/

# Output screenshot: MR_MovieLensAnalysis/output/KPI1_top_n_most_viewed_movies.PNG


#####################################################################################################
### KPI3: We wish to know how have the genres ranked by Average Rating, for each profession and
age group. The age groups to be considered are: 18-35, 36-50 and 50+.
#####################################################################################################

# Logic (on high level):
	1. MapReduce Job: usersAndRatingsJoinerJob
	   A. Input: users.dat and ratings.dat  
	   B. Process: Joins both files on user id, and fetch build data in format of UserID::MovieName::Genres::Rating 
	   C. output: Joined data written to /MovieLens/Input/UserAndRatingJoin/ path 
	
	2. MapReduce Job: genreRankingGeneratorJob
	   A. Input: users.dat and result file from usersAndRatingsJoinerJob i.e. /MovieLens/Input/UserAndRatingJoin/part-r-00000
	   B. Process:
	      i. In mapper: Note, Custom Writable is used to identify file and attach multiple values 
	         a. For file part-r-00000, pass (Key|value) -> (user id | fileName,Genre, Rating)
	         b.  For user.dat, pass (Key|Value) -> (user id | age group, profession)
	      ii. In reducer:
	         a. If file is user.dat, concat profession and age group. There will be only one occurance of this.         
	         b.  If file is part-r-00000, put all values in to map where key as Genre and value is List of Rankings. 
	      iii. At end, reduce populate creates a data structure as Map<String, Map<String, List<Float>>>. 
	           In data term, Map<(profession and age-group), Map<Genre, List<Ratings>>>.
	      iv. Note, context writing is happening in cleanup method.
	      v. In cleanup method:
	         a. Iterate Map<(profession and age-group), Map<Genre, List<Ratings>>> and for each EntrySet
	           - Pass entry.getValue() to a API (getRankedGenres) which does follows:
	           	  - iterates the Map<Genre, List<Ratings>>
	           	  - calculate average rating for each Genre
	           	  - Returns sorted (descending) genre based on average rating
	           - write to context, entrySet.getKey() and sorted genre based in average ratings. 	      

# Command: yarn jar /home/barun/HadoopWorkshopArtifacts/MovieLens/MovieLensJob.jar com.hadoop.mr.movielense.job.GenresRankingJob /MovieLens/Input/movies.dat /MovieLens/Input/ratings.dat /MovieLens/Input/users.dat /MovieLens/Output/GenresRanking/

# Output screenshot: MR_MovieLensAnalysis/output/KPI3_genres_ranked_by_average_rating.PNG