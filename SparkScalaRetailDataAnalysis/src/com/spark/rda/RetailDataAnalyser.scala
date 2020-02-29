package com.spark.rda

import org.apache.spark.sql.SparkSession
import java.lang.Double

object RetailDataAnalyser {
  
  def main(args : Array[String]){
    
    if( args.length < 2){
      System.err.println("Usages: com.spark.rda.RetailDataAnalyser <inPath> <outPath>");
      System.exit(1);
    }
    
    val rdaSession  = SparkSession.builder().appName("RDA").getOrCreate();
    
    val rddData = rdaSession.read.textFile(args(0)).rdd
    
    val result = rddData.map{line => {
      
      //"2012-01-01      09:00   San Jose        Men's Clothing  214.05  Amex"
      val dataTokens = line.trim().split("\t")
      
      val product = dataTokens(3)
      val price = dataTokens(4)
      
      (product, Double.parseDouble(price))
      
    }}
    .reduceByKey(_+_)
    
    result.saveAsTextFile(args(1))
    
    rdaSession.stop
    
  }
}

//bin/spark-submit --class com.spark.rda.RetailDataAnalyser /home/barun/SparkWorkshopArtifacts/RetailData/sparkRDAJob.jar /home/barun/SparkWorkshopArtifacts/RetailData/Retail_Sample_Data_Set.txt /home/barun/SparkWorkshopArtifacts/RetailData/rda-out-001