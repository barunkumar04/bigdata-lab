package com.spark.setupbox.kpi3

import org.apache.spark.sql.SparkSession
import scala.xml.XML
import org.apache.spark.rdd.RDD.rddToOrderedRDDFunctions
import java.lang.Double

/**
 * 1. Filter all the record with event_id=100
 * 		i. Get the top five Channels with maximum duration
 *
 */

object KPI3_1_MaxPriceGroupedByOfferID {

  def main(args: Array[String]) = {

    System.setProperty("hadoop.home.dir", "E:\\Study&Workshop\\ExternalJars\\hadoop-2.5.0-cdh5.3.2")
    System.setProperty("spark.sql.warehouse.dir", "file:/E:/Study&Workshop/ExternalJars/spark-2.0.2-bin-hadoop2.6/spark-2.0.2-bin-hadoop2.6/spark-warehouse")

    val spark = SparkSession
      .builder
      .appName("KPI3_1_MaxPriceGroupedByOfferID")
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
      if (event_id.length() > 0 && (event_id.equals("102") || event_id.equals("113"))) {
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
      
      
      
      val price =((xml \ "nv").filter(node => (node \@ "n") == "Price")) \@ "v" 
      
      var priceNum = 0.0
      if(price != null & price.length() > 0 )
        priceNum = Double.valueOf(price)
      
      val offerId =((xml \ "nv").filter(node => (node \@ "n") == "OfferId")) \@ "v" 
      
      var interestedOfferId = "" 
      if(offerId != null && offerId.length() > 0 ){
        interestedOfferId = offerId
      }
      (interestedOfferId,priceNum) 
       
    })
    .filter(touple => {touple._1.length() > 0})
    .reduceByKey(_+_)
    .groupByKey() // [OfferID, [4.00, 0.0, 3.0,...., 5.0]] 
    .map( touple => {
      (touple._1, touple._2.reduce((price1, price2) => price1 + price2))
    })
    .sortBy(_._2,false)
    .take(5)
    
    println("Top 5 max duration channels are: ")
    result.foreach(println)
    
    spark.stop()
  }

}