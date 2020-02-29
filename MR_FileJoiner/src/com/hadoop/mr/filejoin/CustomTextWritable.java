package com.hadoop.mr.filejoin;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

public class CustomTextWritable implements Writable{
	
	
	Text fileName = null;
	Text value = null;
	
	public CustomTextWritable() {
		fileName = new Text();
		value = new Text();
	}
	
	public CustomTextWritable(Text fileName, Text value) {
		this.fileName = fileName;
		this.value = value;
	}


	@Override
	public void readFields(DataInput in) throws IOException {
		this.fileName.readFields(in);
		this.value.readFields(in);
	}


	@Override
	public void write(DataOutput out) throws IOException {
		this.fileName.write(out);
		this.value.write(out);
	}
	
	
	
}
