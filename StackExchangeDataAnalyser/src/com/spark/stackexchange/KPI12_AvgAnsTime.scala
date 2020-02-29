//12. Average time for a post to get a correct answer

package com.spark.stackexchange

import scala.xml.XML

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

import java.text.SimpleDateFormat
import java.lang.String
import java.util.Date
import org.apache.spark.sql.SparkSession

object KPI12_AvgAnsTime {
	def main(args: Array[String]) = {
			System.setProperty("hadoop.home.dir", "E:\\Study&Workshop\\ExternalJars\\hadoop-2.5.0-cdh5.3.2")
			System.setProperty("spark.sql.warehouse.dir", "file:/E:/Study&Workshop/ExternalJars/spark-2.0.2-bin-hadoop2.6/spark-2.0.2-bin-hadoop2.6/spark-warehouse")

			val format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
      val format2 = new SimpleDateFormat("yyyy-MM");

			val spark = SparkSession
				.builder
				.appName("AvgAnsTime")
				.master("local")
				.getOrCreate()
				
			//Read some example file to a test RDD
			val data = spark.read.textFile("E:\\Study&Workshop\\SparkWorkshopArtifacts\\StackOverflow\\Posts.xml").rdd

			val baseData = data.filter{line => {line.trim().startsWith("<row")}
			}
			.map {line => {
			  val xml = XML.loadString(line)
			  var aaId = "";
			  if (xml.attribute("AcceptedAnswerId") != None)
			  {
			    aaId = xml.attribute("AcceptedAnswerId").get.toString()
			  }
			  val crDate = xml.attribute("CreationDate").get.toString()
			  val rId = xml.attribute("Id").get.toString()
//			  (closeDate, line)
			  (rId, aaId, crDate)
			  }
			}
			
			val aaData = baseData.map{ data => {
			  (data._2, data._3)
			}
			}
			.filter{ data => {data._1.length() > 0}}
			
			val rdata = baseData.map{ data => {
			  (data._1, data._3)
			}
			}
			val joinData = rdata.join(aaData)
			.map{ data => {
			  val quesDate = format.parse(data._2._2).getTime
			  val ansDate = format.parse(data._2._1).getTime
			  val diff : Float = ansDate - quesDate
			  val time : Float = diff/(1000 * 60 * 60)    //millisecond to hour
//			  (data, time)
			  time
			}
			}
			val count = joinData.count()
			val result = joinData.sum() / count

			println(result)
			spark.stop
	}
}