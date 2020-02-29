package com.spark.wba

import org.apache.spark.sql.SparkSession
import java.lang.Long
import scala.tools.scalap.scalax.util.StringUtil

//Internet usage grown - Country where Internet usage has grown the most in the past decade
object HighestInternetUsagesGrowth {

  def main(args: Array[String]) {

    if (args.length < 2) {
      System.err.println("Usages: com.spark.wba.HighestInternetUsagesGrowth <inPath> <outPath>");
      System.exit(1);
    }
    val EMPTY_STRING = "";
    
    val session = SparkSession.builder().appName("HighestInternetUsagesGrowth").getOrCreate();

    val wbDataRDD = session.read.csv(args(0)).rdd

    val usages2000RDD = wbDataRDD.map { record =>
      {
        // "Bahamas, The",7/1/2000,,,"31,524",8,17,"1,072",6,"297,651","244,074",18,74,69,72,29,65,5,"6,327,552,000","21,258"
        
        val countryName = record.getString(0)
        val year = record.getString(1)
        
        val internetUsages = record.getString(5)
        var internetUsagesNum = 0
        
        if (internetUsages != null && internetUsages.length() > 0) {
          internetUsagesNum = Integer.parseInt(internetUsages.replace(",", ""))
        }
        if(year.contains("2000"))    
          (countryName, internetUsagesNum)
        else
          (EMPTY_STRING, 0)
      }
    }
    
    val usages2010RDD = wbDataRDD.map { record =>
      {
        // "Bahamas, The",7/1/2000,,,"31,524",8,17,"1,072",6,"297,651","244,074",18,74,69,72,29,65,5,"6,327,552,000","21,258"
        
        val countryName = record.getString(0)
        val year = record.getString(1)
        
        val internetUsages = record.getString(5)
        var internetUsagesNum = 0
        
        if (internetUsages != null && internetUsages.length() > 0) {
          internetUsagesNum = Integer.parseInt(internetUsages.replace(",", ""))
        }
        if(year.contains("2010"))    
          (countryName, internetUsagesNum)
        else
          (EMPTY_STRING, 0)
      }
    }
    
    
    val resultArray = usages2010RDD.join(usages2000RDD).collect().map(rec => {
      //rec: (India, (1684323716503, 1361057169927))
      (rec._1, (rec._2._1 - rec._2._2))
    })
    
    val resultRDD = session.sparkContext.parallelize(resultArray).sortBy(_._2, false)
    
    val sortedResultRDD = session.sparkContext.parallelize(resultRDD.take(10))
    
    sortedResultRDD.saveAsTextFile(args(1))
    
    session.stop

  }
}

//bin/spark-submit --class com.spark.wba.HighestInternetUsagesGrowth /home/barun/SparkWorkshopArtifacts/WorldBank/sparkWBDataAnalyserJob.jar /home/barun/SparkWorkshopArtifacts/WorldBank/World_Bank_Indicators.csv /home/barun/SparkWorkshopArtifacts/WorldBank/HighestInternetUsagesGrowth-out-001