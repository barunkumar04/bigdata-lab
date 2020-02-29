//1. Count the total number of questions in the available data-set and collect the questions id of all the questions
package com.spark.stackexchange

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object KPI1_QuesCount {
	def main(args: Array[String]) = {
			System.setProperty("hadoop.home.dir", "E:\\Study&Workshop\\ExternalJars\\hadoop-2.5.0-cdh5.3.2")
			System.setProperty("spark.sql.warehouse.dir", "file:/E:/Study&Workshop/ExternalJars/spark-2.0.2-bin-hadoop2.6/spark-2.0.2-bin-hadoop2.6/spark-warehouse")

			val spark = SparkSession
				.builder
				.appName("QuesCount")
				.master("local")
				.getOrCreate()
				
			//Read some example file to a test RDD
			val data = spark.read.textFile("E:\\Study&Workshop\\SparkWorkshopArtifacts\\StackOverflow\\Posts.xml").rdd


			val result = data.filter{line => {line.trim().startsWith("<row")}			   
			}
			.filter { line => {line.contains("PostTypeId=\"1\"")}
			}
			
			result.foreach { println }
			println("Total Count: " + result.count())

			spark.stop
	}
}
//http://public-repo-1.hortonworks.com/hdp-win-alpha/winutils.exe