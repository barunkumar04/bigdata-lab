package com.spark.setupbox.kpi1

import org.apache.spark.sql.SparkSession
import scala.xml.XML

/**
 * 1. Filter all the record with event_id=100
 * 		iii. Total number of devices with ChannelType="LiveTVMediaChannel"
 *
 */

object KPI1_3_DeviceCountOfASpecificChannelType {

  def main(args: Array[String]) = {

    System.setProperty("hadoop.home.dir", "E:\\Study&Workshop\\ExternalJars\\hadoop-2.5.0-cdh5.3.2")
    System.setProperty("spark.sql.warehouse.dir", "file:/E:/Study&Workshop/ExternalJars/spark-2.0.2-bin-hadoop2.6/spark-2.0.2-bin-hadoop2.6/spark-warehouse")

    val spark = SparkSession
      .builder
      .appName("KPI1_3_DeviceCountOfASpecificChannelType")
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
      
      var channelType_LiveTVMediaChannel = ""
      
      val channelType =((xml \ "nv").filter(node => (node \@ "n") == "ChannelType")) \@ "v" 
      
      if(channelType != null && channelType.length() > 0 && channelType.equals("LiveTVMediaChannel")){
        channelType_LiveTVMediaChannel = channelType
      }
      (interestedEventId,channelType_LiveTVMediaChannel,deviceId) 
       
    })
    .filter(touple => {touple._1.length() > 0 && touple._2.length() > 0 }).count()
    
    println("Total count of channel type 'LiveTVMediaChannel' is: "+result)
    
    spark.stop()
  }

}