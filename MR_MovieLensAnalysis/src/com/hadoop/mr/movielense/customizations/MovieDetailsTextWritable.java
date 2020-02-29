package com.hadoop.mr.movielense.customizations;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

public class MovieDetailsTextWritable implements Writable{
	
	
	private Text fileName = null;
	private Text userID = null;
	private Text movieName = null;
	private FloatWritable rating = null;
	private Text genres = null;
	private Text occupation = null;
	private Text ageGroup = null;
	private BooleanWritable isValid = null;
	
	
	public MovieDetailsTextWritable() {
		fileName = new Text();
		userID = new Text();
		movieName = new Text();
		rating = new FloatWritable();
		genres = new Text();
		occupation = new Text();
		ageGroup = new Text();
		isValid = new BooleanWritable();
	}


	@Override
	public void readFields(DataInput in) throws IOException {
		this.fileName.readFields(in);
		this.userID.readFields(in);
		this.movieName.readFields(in);
		this.rating.readFields(in);
		this.genres.readFields(in);
		this.occupation.readFields(in);
		this.ageGroup.readFields(in);
		this.isValid.readFields(in);
	}


	@Override
	public void write(DataOutput out) throws IOException {
		this.fileName.write(out);
		this.userID.write(out);
		this.movieName.write(out);
		this.rating.write(out);
		this.genres.write(out);
		this.occupation.write(out);
		this.ageGroup.write(out);
		this.isValid.write(out);
	}
	
	
	
	public FloatWritable getRating() {
		return rating;
	}

	public void setRating(FloatWritable rating) {
		this.rating = rating;
	}

	public void setFileName(Text fileName) {
		this.fileName = fileName;
	}


	public void setIsValid(BooleanWritable isValid) {
		this.isValid = isValid;
	}

	public Text getFileName() {
		return fileName;
	}


	public BooleanWritable getIsValid() {
		return isValid;
	}


	public Text getUserID() {
		return userID;
	}


	public void setUserID(Text userID) {
		this.userID = userID;
	}


	public Text getMovieName() {
		return movieName;
	}


	public void setMovieName(Text movieName) {
		this.movieName = movieName;
	}


	public Text getGenres() {
		return genres;
	}


	public void setGenres(Text genres) {
		this.genres = genres;
	}


	public Text getOccupation() {
		return occupation;
	}


	public void setOccupation(Text occupation) {
		this.occupation = occupation;
	}


	public Text getAgeGroup() {
		return ageGroup;
	}


	public void setAgeGroup(Text ageGroup) {
		this.ageGroup = ageGroup;
	}
	
}
