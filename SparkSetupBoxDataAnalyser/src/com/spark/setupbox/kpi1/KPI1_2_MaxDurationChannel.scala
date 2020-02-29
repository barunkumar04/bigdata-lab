package com.spark.setupbox.kpi1

import org.apache.spark.sql.SparkSession
import scala.xml.XML
import org.apache.spark.rdd.RDD.rddToOrderedRDDFunctions

/**
 * 1. Filter all the record with event_id=100
 * 		i. Get the top five Channels with maximum duration
 *
 */

object KPI1_2_MaxDurationChannel {

  def main(args: Array[String]) = {

    System.setProperty("hadoop.home.dir", "E:\\Study&Workshop\\ExternalJars\\hadoop-2.5.0-cdh5.3.2")
    System.setProperty("spark.sql.warehouse.dir", "file:/E:/Study&Workshop/ExternalJars/spark-2.0.2-bin-hadoop2.6/spark-2.0.2-bin-hadoop2.6/spark-warehouse")

    val spark = SparkSession
      .builder
      .appName("KPI1_2_MaxDurationChannel")
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
      if (event_id.length() > 0 && event_id.equals("100")) {
        interestedEventId = event_id
      }


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
      
      
      
      val channel =((xml \ "nv").filter(node => (node \@ "n") == "ChannelNumber")) \@ "v" 
      
      
      var duraionNum = 0
      val duration =((xml \ "nv").filter(node => (node \@ "n") == "Duration")) \@ "v" 
      
      if(duration != null && duration.length() > 0 ){
        duraionNum = Integer.parseInt(duration)
      }
      (interestedEventId,duraionNum,channel) 
       
    })
    .filter(touple => {touple._1.length() > 0})
    .map(touple => {
      (touple._2, touple._3)
    })
    .sortByKey(false, 1)
    .take(5)
    
    println("Top 5 max duration channels are: ")
    result.foreach(println)
    
    spark.stop()
  }

}