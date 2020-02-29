package com.spark.stackexchange

import org.apache.spark.sql.SparkSession
import scala.xml.XML
import java.text.SimpleDateFormat
import java.sql.Date

//Questions which are marked closed for each category � provide the distribution of number of closed questions per month
object KPI8_QuestionsClosedPerMonth {

  def main(args: Array[String]) = {

    System.setProperty("hadoop.home.dir", "E:\\Study&Workshop\\ExternalJars\\hadoop-2.5.0-cdh5.3.2")
    System.setProperty("spark.sql.warehouse.dir", "file:/E:/Study&Workshop/ExternalJars/spark-2.0.2-bin-hadoop2.6/spark-2.0.2-bin-hadoop2.6/spark-warehouse")

    val spark = SparkSession
      .builder
      .appName("KPI7_QuestionsActiveFor6Months")
      .master("local")
      .getOrCreate()

    val data = spark.read.textFile("E:\\Study&Workshop\\SparkWorkshopArtifacts\\StackOverflow\\Posts.xml").rdd

    val dateFormatterWithDateAndTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
    val dateFormatterWithYearAndMonth = new SimpleDateFormat("yyyy-MM")
    val MILISEC_IN_A_DAY = 1000 * 60 * 60 * 24

    //<row Id="530" PostTypeId="1" AcceptedAnswerId="532" CreationDate="2014-06-23T04:39:26.623" Score="4" ViewCount="269" Body="&lt;p&gt;There is a general recommendation that algorithms in ensemble learning combinations should be different in nature. Is there a classification table, a scale or some rules that allow to evaluate how far away are the algorithms from each other? What are the best combinations? &lt;/p&gt;&#xA;" OwnerUserId="454" LastActivityDate="2014-06-24T15:44:52.540" Title="How to select algorithms for ensemble methods?" Tags="&lt;machine-learning&gt;" AnswerCount="2" CommentCount="0" />
    val result = data.filter { line => { line.trim().startsWith("<row") } }
      .filter { line => { line.contains("PostTypeId=\"1\"") } }
      .map { line =>
        val xml = XML.loadString(line)

        //2014-05-13T23:58:30.457
        var closedDateYearMonth = ""
        if (xml.attribute("ClosedDate") != None) {
          val closedDateStr = xml.attribute("ClosedDate").get.toString()
          val closedDateTemp = dateFormatterWithDateAndTime.parse(closedDateStr)
          closedDateYearMonth = dateFormatterWithYearAndMonth.format(closedDateTemp)

        }

        (closedDateYearMonth, 1)

      }
      .filter { touple => { touple._1.length() > 0 } }
      .reduceByKey(_ + _)

    result.foreach(println)

    System.out.println(result.count())

    spark.stop
  }

}