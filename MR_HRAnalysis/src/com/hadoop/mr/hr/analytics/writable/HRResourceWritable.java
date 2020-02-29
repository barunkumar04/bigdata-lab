package com.hadoop.mr.hr.analytics.writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

public class HRResourceWritable implements Writable{

	private BooleanWritable hasPromoted = null;
	private BooleanWritable hasResigned = null;
	private FloatWritable satisfactionLevel = null;
	private FloatWritable avgWorkingHours = null;
	private FloatWritable evaluation = null;
	private Text salaryCategory = null;
	private IntWritable experience = null;
	
	public HRResourceWritable() {
		hasPromoted = new BooleanWritable();
		hasResigned = new BooleanWritable();
		satisfactionLevel = new FloatWritable();
		avgWorkingHours = new FloatWritable();
		evaluation =  new FloatWritable();
		salaryCategory = new Text();
		experience = new IntWritable();
	}
	
	public HRResourceWritable(Boolean hasPromoted, Boolean hasResigned, Float avgSatisfactionLevel, Float avgWorkingHours) {
		
		this.hasPromoted = new BooleanWritable(hasPromoted);
		this.hasResigned = new BooleanWritable(hasResigned);
		this.satisfactionLevel = new FloatWritable(avgSatisfactionLevel);
		this.avgWorkingHours = new FloatWritable(avgWorkingHours);
	}
	
	public BooleanWritable getHasPromoted() {
		return hasPromoted;
	}

	public BooleanWritable getHasResigned() {
		return hasResigned;
	}

	
	
	public FloatWritable getAvgSatisfactionLevel() {
		return satisfactionLevel;
	}

	public FloatWritable getAvgWorkingHours() {
		return avgWorkingHours;
	}

	
	
	public void setHasPromoted(BooleanWritable hasPromoted) {
		this.hasPromoted = hasPromoted;
	}

	public void setHasResigned(BooleanWritable hasResigned) {
		this.hasResigned = hasResigned;
	}

	public void setSatisfactionLevel(FloatWritable satisfactionLevel) {
		this.satisfactionLevel = satisfactionLevel;
	}

	public void setAvgWorkingHours(FloatWritable avgWorkingHours) {
		this.avgWorkingHours = avgWorkingHours;
	}

	
	
	public FloatWritable getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(FloatWritable evaluation) {
		this.evaluation = evaluation;
	}

	public FloatWritable getSatisfactionLevel() {
		return satisfactionLevel;
	}

	
	
	public Text getSalaryCategory() {
		return salaryCategory;
	}

	public void setSalaryCategory(Text salaryCategory) {
		this.salaryCategory = salaryCategory;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		hasPromoted.readFields(in);
		hasResigned.readFields(in);
		satisfactionLevel.readFields(in);
		avgWorkingHours.readFields(in);
		evaluation.readFields(in);
		salaryCategory.readFields(in);
		experience.readFields(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		hasPromoted.write(out);
		hasResigned.write(out);
		satisfactionLevel.write(out);
		avgWorkingHours.write(out);
		evaluation.write(out);
		salaryCategory.write(out);
		experience.write(out);
	}

	public IntWritable getExperience() {
		return experience;
	}

	public void setExperience(IntWritable experience) {
		this.experience = experience;
	}

}
