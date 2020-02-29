package com.spark.setupbox.kpi4

import org.apache.spark.sql.SparkSession
import scala.xml.XML
import org.apache.spark.rdd.RDD.rddToOrderedRDDFunctions

/**
 * 1. Filter all the record with event_id=100
 * 		i. Get the top five devices with maximum duration
 *
 *
 */

object KPI4_1_MinAndMaxDuration {

  def main(args: Array[String]) = {

    System.setProperty("hadoop.home.dir", "E:\\Study&Workshop\\ExternalJars\\hadoop-2.5.0-cdh5.3.2")
    System.setProperty("spark.sql.warehouse.dir", "file:/E:/Study&Workshop/ExternalJars/spark-2.0.2-bin-hadoop2.6/spark-2.0.2-bin-hadoop2.6/spark-warehouse")

    val spark = SparkSession
      .builder
      .appName("SetupBox")
      .master("local")
      .getOrCreate()

    val data = spark.read.textFile("E:\\Study&Workshop\\SparkWorkshopArtifacts\\Setupbox\\Set_Top_Box_Data.txt").rdd

    val result = data.filter( line => {
       val splits = line.replace("^", "|").split("\\|");
       splits.length == 7
    })
    .map(line => {
       val splits = line.replace("^", "|").split("\\|");
      
       val event_id = splits(2)
      var interestedEventId = ""
      if (event_id.length() > 0 && event_id.equals("118")) {
        interestedEventId = event_id
      }

      val deviceId = splits(5)

      val nameValueXML = splits(4)
      /**
       * <d>	<nv n="ExtStationID" v="Station/FYI Television, Inc./25102" />
       * 			<nv n="MediaDesc" v="19b8f4c0-92ce-44a7-a403-df4ee413aca9" />
       * 			<nv n="ChannelNumber" v="1366" />
       * 			<nv n="Duration" v="24375" />
       * 			<nv n="IsTunedToService" v="True" />
       * 			.............
       * 			.............
       * </d>
       */
      val xml = XML.loadString(nameValueXML)
      
      var duraionNum = 0
      val duration =((xml \ "nv").filter(node => (node \@ "n") == "DurationSecs")) \@ "v" 
      
      if(duration != null && duration.length() > 0 ){
        duraionNum = Integer.parseInt(duration)
      }
      (interestedEventId,duraionNum) 
       
    })
    .filter(touple => {touple._1.length() > 0})
    .groupByKey()
    .map(touple => {
      (touple._1, touple._2.min, touple._2.max)
    })
    
    println("Min and Max duration for event id 118 in sec: ")
    result.foreach(println)
    
    spark.stop()
  }

}