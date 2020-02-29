package com.hadoop.mr.hr.analytics.partitioner;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

import com.hadoop.mr.hr.analytics.writable.HRResourceWritable;

public class ExperianceWisePartitioner extends Partitioner<Text, HRResourceWritable> {

	private int EXP_2 = 0;
	private int EXP_3 = 1;
	private int EXP_4 = 2;
	private int EXP_5 = 3;
	private int EXP_6 = 4;
	private int EXP_7 = 5;
	private int EXP_8 = 6;
	private int EXP_10 = 7;

	@Override
	public int getPartition(Text arg0, HRResourceWritable hrResourceWritable, int reducerTaskCount) {

		if (reducerTaskCount != 0) {
			switch (hrResourceWritable.getExperience().get()) {
			case 2:
				return EXP_2;
			case 3:
				return EXP_3;
			case 4:
				return EXP_4;
			case 5:
				return EXP_5;
			case 6:
				return EXP_6;
			case 7:
				return EXP_7;
			case 8:
				return EXP_8;
			case 10:
				return EXP_10;
			}
		}
		return 0;
	}

}
