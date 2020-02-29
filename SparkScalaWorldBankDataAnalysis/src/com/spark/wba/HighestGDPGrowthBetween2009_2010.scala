package com.spark.wba

import org.apache.spark.sql.SparkSession
import java.lang.Long
import scala.tools.scalap.scalax.util.StringUtil
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.DoubleType
import org.apache.spark.sql.types.LongType

//Highest GDP growth - List of Countries with highest GDP growth from 2009 to 2010 in descending order
object HighestGDPGrowthBetween2009_2010 {

  def main(args: Array[String]) {

    if (args.length < 2) {
      System.err.println("Usages: com.spark.wba.HighestGDPGrowthBetween2009_2010 <inPath> <outPath>");
      System.exit(1);
    }
    val EMPTY_STRING = "";
    
    val session = SparkSession.builder().appName("HighestGDPGrowthBetween2009_2010").getOrCreate();

    val wbDataRDD = session.read.csv(args(0)).rdd

    val gdp2009RDD = wbDataRDD.map { record =>
      {
        // "Bahamas, The",7/1/2000,,,"31,524",8,17,"1,072",6,"297,651","244,074",18,74,69,72,29,65,5,"6,327,552,000","21,258"
        
        val countryName = record.getString(0)
        val year = record.getString(1)
        
        val gdp = record.getString(18)
        var gdpNum = 0L
        
        if (gdp != null && gdp.length() > 0) {
          gdpNum = Long.parseLong(gdp.replace(",", ""))
        }
        if(year.contains("2009"))    
          (countryName, gdpNum)
        else
          (EMPTY_STRING, 0L)
      }
    }
    
    val gdp2010RDD = wbDataRDD.map { record =>
      {
        // "Bahamas, The",7/1/2000,,,"31,524",8,17,"1,072",6,"297,651","244,074",18,74,69,72,29,65,5,"6,327,552,000","21,258"
        
        val countryName = record.getString(0)
        val year = record.getString(1)
        
        val gdp = record.getString(18)
        var gdpNum = 0L
        
        if (gdp != null && gdp.length() > 0) {
          gdpNum = Long.parseLong(gdp.replace(",", ""))
        }
        if(year.contains("2010"))    
          (countryName, gdpNum)
        else
          (EMPTY_STRING, 0L)
      }
    }
    
    //val resultRdd = gdp2010RDD.subtract(gdp2009RDD)
    
    val resultArray = gdp2010RDD.join(gdp2009RDD).collect().map(rec => {
      //rec: (India, (1684323716503, 1361057169927))
      (rec._1, (rec._2._1 - rec._2._2))
    })
    
    val resultRDD = session.sparkContext.parallelize(resultArray).sortBy(_._2, false)
    
    val sortedResultRDD = session.sparkContext.parallelize(resultRDD.take(10))
    
    sortedResultRDD.saveAsTextFile(args(1))
    
    session.stop

  }
}

//bin/spark-submit --class com.spark.wba.HighestGDPGrowthBetween2009_2010 /home/barun/SparkWorkshopArtifacts/WorldBank/sparkWBDataAnalyserJob.jar /home/barun/SparkWorkshopArtifacts/WorldBank/World_Bank_Indicators.csv /home/barun/SparkWorkshopArtifacts/WorldBank/HighestGDPGrowthBetween2009_2010-out-001