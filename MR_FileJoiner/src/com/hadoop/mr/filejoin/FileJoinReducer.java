package com.hadoop.mr.filejoin;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class FileJoinReducer extends Reducer<Text, CustomTextWritable, NullWritable, Text>{

	@Override
	protected void reduce(Text id, Iterable<CustomTextWritable> listOfNameOrDept, Context context)
			throws IOException, InterruptedException {
		
		Iterator itr = listOfNameOrDept.iterator();
		String dept = null;
		String name = null;
		while(itr.hasNext()) {
			CustomTextWritable nameOrDept = (CustomTextWritable) itr.next();
			if(nameOrDept.fileName.toString().contains("empdept.txt")) {
				dept = nameOrDept.value.toString();
			}
			if(nameOrDept.fileName.toString().contains("empname.txt")) {
				name = nameOrDept.value.toString();
			}
		}
		context.write(NullWritable.get(), new Text(id.toString()+","+name+","+dept));
	}

	
}
