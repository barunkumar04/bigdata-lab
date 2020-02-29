//1.Calculate sales breakdown by product category across all of the stores.
package com.spark.retaildata;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.SparkSession;

import scala.Tuple2;

public final class ProductSales {

	public static void main(String[] args) throws Exception {
		
		if (args.length < 2) {
			System.err.println("Usage: Retail Analysis <INP-PATH> <OUTPUT-PATH>");
			System.exit(1);
		}

		SparkSession spark = SparkSession.builder().appName("JavaWordCount").getOrCreate();

		JavaRDD<String> lines = spark.read().textFile(args[0]).javaRDD();

		JavaPairRDD<String, Float> pairData = lines.mapToPair(new PairFunction<String, String, Float>() {
			@Override
			public Tuple2<String, Float> call(String s) {
//				"2012-01-01      09:00   San Jose        Men's Clothing  214.05  Amex"
				final String[] dataTokens = s.trim().split("\t");
				return new Tuple2<String, Float>(dataTokens[3], Float.parseFloat(dataTokens[4]));
			}
		});

		JavaPairRDD<String, Float> result = pairData.reduceByKey(new Function2<Float, Float, Float>() {
			@Override
			public Float call(Float i1, Float i2) {
				return i1 + i2;
			}
		});
		
		result.saveAsTextFile(args[1]);
		spark.stop();
	}
}
//bin/spark-submit --class com.spark.retaildata.ProductSales ../retailDataAnalysis.jar ../Retail_Sample_Data_Set.txt ret-out-01