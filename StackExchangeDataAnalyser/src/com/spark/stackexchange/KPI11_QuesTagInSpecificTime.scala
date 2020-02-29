//11. Number of question with specific tags (nosql, big data) which was asked in the specified time range (from 01-01-2015 to 31-12-2015)

package com.spark.stackexchange

import scala.xml.XML

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

import java.text.SimpleDateFormat
import java.lang.String
import java.util.Date
import org.apache.spark.sql.SparkSession

object KPI11_QuesTagInSpecificTime {
	def main(args: Array[String]) = {
			System.setProperty("hadoop.home.dir", "E:\\Study&Workshop\\ExternalJars\\hadoop-2.5.0-cdh5.3.2")
    System.setProperty("spark.sql.warehouse.dir", "file:/E:/Study&Workshop/ExternalJars/spark-2.0.2-bin-hadoop2.6/spark-2.0.2-bin-hadoop2.6/spark-warehouse")

			val format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
      val format2 = new SimpleDateFormat("yyyy-MM");
      val format3 = new SimpleDateFormat("yyyy-MM-dd");
      
      val startTime = format3.parse("2015-01-01").getTime
      val endTime = format3.parse("2015-01-31").getTime
      
			val spark = SparkSession
				.builder
				.appName("AvgAnsTime")
				.master("local")
				.getOrCreate()
				
			//Read some example file to a test RDD
			val data = spark.read.textFile("E:\\Study&Workshop\\SparkWorkshopArtifacts\\StackOverflow\\Posts.xml").rdd


			val result = data.filter{line => {line.trim().startsWith("<row")}
			}
			.filter { line => {line.contains("PostTypeId=\"1\"")}
			}
			.map {line => {
			  val xml = XML.loadString(line)
			  val crDate = xml.attribute("CreationDate").get.toString()
			  val tags = xml.attribute("Tags").get.toString()
//			  (closeDate, line)
			  (crDate, tags, line)
			  }
			}
			.filter{ data => {
			  var flag = false
			  val crTime = format.parse(data._1.toString()).getTime
			  if (crTime > startTime && crTime < endTime && (data._2.toLowerCase().contains("bigdata") || 
			      data._2.toLowerCase().contains("hadoop") || data._2.toLowerCase().contains("spark")))
			    flag = true
			  flag
			  }
			}
		
			result.foreach { println }
			println(result.count())
			
			spark.stop
	}
}