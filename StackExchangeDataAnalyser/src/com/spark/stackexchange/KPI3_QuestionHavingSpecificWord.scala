package com.spark.stackexchange

import org.apache.spark.sql.SparkSession
import scala.xml.XML

object KPI3_QuestionHavingSpecificWord {

  def main(args: Array[String]) = {

    System.setProperty("hadoop.home.dir", "E:\\Study&Workshop\\ExternalJars\\hadoop-2.5.0-cdh5.3.2")
    System.setProperty("spark.sql.warehouse.dir", "file:/E:/Study&Workshop/ExternalJars/spark-2.0.2-bin-hadoop2.6/spark-2.0.2-bin-hadoop2.6/spark-warehouse")

    val spark = SparkSession
      .builder
      .appName("AvgAnsTime")
      .master("local")
      .getOrCreate()

    val data = spark.read.textFile("E:\\Study&Workshop\\SparkWorkshopArtifacts\\StackOverflow\\Posts.xml").rdd

    val result = data.filter{line => {line.trim().startsWith("<row")}
			}
			.filter { line => {line.contains("PostTypeId=\"1\"")}
			}
			.flatMap {line => {
			  val xml = XML.loadString(line)
			  xml.attribute("Title")
			  }
			}
			.filter { line => {
			  line.mkString.toLowerCase().contains("hadoop")
			}
			}
			
			result.foreach { println }
			println ("Result Count: " + result.count())
			
			spark.stop
  }

}