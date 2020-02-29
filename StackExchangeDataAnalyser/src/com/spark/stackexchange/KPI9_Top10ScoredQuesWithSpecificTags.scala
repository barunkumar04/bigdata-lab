package com.spark.stackexchange

import org.apache.spark.sql.SparkSession
import scala.xml.XML
import java.text.SimpleDateFormat
import java.sql.Date

//Questions which are marked closed for each category – provide the distribution of number of closed questions per month
object KPI9_Top10ScoredQuesWithSpecificTags {

  def main(args: Array[String]) = {

    System.setProperty("hadoop.home.dir", "E:\\Study&Workshop\\ExternalJars\\hadoop-2.5.0-cdh5.3.2")
    System.setProperty("spark.sql.warehouse.dir", "file:/E:/Study&Workshop/ExternalJars/spark-2.0.2-bin-hadoop2.6/spark-2.0.2-bin-hadoop2.6/spark-warehouse")

    val spark = SparkSession
      .builder
      .appName("KPI9_Top10ScoredQuesWithSpecificTags")
      .master("local")
      .getOrCreate()

    val data = spark.read.textFile("E:\\Study&Workshop\\SparkWorkshopArtifacts\\StackOverflow\\Posts.xml").rdd

    //<row Id="530" PostTypeId="1" AcceptedAnswerId="532" CreationDate="2014-06-23T04:39:26.623" Score="4" ViewCount="269" Body="&lt;p&gt;There is a general recommendation that algorithms in ensemble learning combinations should be different in nature. Is there a classification table, a scale or some rules that allow to evaluate how far away are the algorithms from each other? What are the best combinations? &lt;/p&gt;&#xA;" OwnerUserId="454" LastActivityDate="2014-06-24T15:44:52.540" Title="How to select algorithms for ensemble methods?" Tags="&lt;machine-learning&gt;" AnswerCount="2" CommentCount="0" />
    val result = data.filter { line => { line.trim().startsWith("<row") } }
      .filter { line => { line.contains("PostTypeId=\"1\"") } }
      .map { line =>
        val xml = XML.loadString(line)

        val score = xml.attribute("Score").get.toString()
        val tag = xml.attribute("Tags").get.toString()

        var expectedTag = ""

        if (tag.toLowerCase().contains("hadoop") || tag.toLowerCase().contains("spark")) {
          expectedTag = tag;
        }

        (Integer.parseInt(score), expectedTag, line)

      }
      .filter { touple => { touple._2.length() > 0 } }
      .sortBy(touple => {touple._1}, false)
      .map(touple => {
        (touple._1, touple._3)
      })
      .take(10)

    result.foreach(println)

    spark.stop
  }

}