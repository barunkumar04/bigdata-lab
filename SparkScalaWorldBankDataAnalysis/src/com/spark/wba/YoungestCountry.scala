package com.spark.wba

import org.apache.spark.sql.SparkSession
import java.lang.Long
import scala.tools.scalap.scalax.util.StringUtil

//Youngest Country - Yearly distribution of youngest Countries
object YoungCountry {

  def main(args: Array[String]) {

    if (args.length < 2) {
      System.err.println("Usages: com.spark.wba.YoungCountry <inPath> <outPath>");
      System.exit(1);
    }
    val EMPTY_STRING = "";

    val session = SparkSession.builder().appName("YoungCountry").getOrCreate();

    val wbDataRDD = session.read.csv(args(0)).rdd

    val totalYoungPopulationArray = wbDataRDD.map { record =>
      {
        // "Bahamas, The",7/1/2000,,,"31,524",8,17,"1,072",6,"297,651","244,074",18,74,69,72,29,65,5,"6,327,552,000","21,258"

        val countryName = record.getString(0)
        val year = record.getString(1)

        val youngestPopulationPercentage = record.getString(15)
        val totalPopulation = record.getString(9)

        var youngestPopulationPercentageNum = 0L
        var totalPopulationNum = 0L

        if (youngestPopulationPercentage != null && youngestPopulationPercentage.length() > 0) {
          youngestPopulationPercentageNum = Long.parseLong(youngestPopulationPercentage.replace(",", ""))
        }

        if (totalPopulation != null && totalPopulation.length() > 0) {
          totalPopulationNum = Long.parseLong(totalPopulation.replace(",", ""))
        }

        val totalYoungestPopulation = (totalPopulationNum * youngestPopulationPercentageNum) / 100

        if (year.contains("2010"))
          (countryName, totalYoungestPopulation)
        else
          (EMPTY_STRING, 0L)
      }
    }.sortBy(_._2, false).take(3).map(rec => { (rec._1) })

    val yearlyDistributionRDD = wbDataRDD.map { record =>
      {
        // "Bahamas, The",7/1/2000,,,"31,524",8,17,"1,072",6,"297,651","244,074",18,74,69,72,29,65,5,"6,327,552,000","21,258"

        val countryName = record.getString(0)
        val year = record.getString(1)
        val totalPopulation = record.getString(9)

        var youngestPopulationPercentageNum = 0L
        var totalPopulationNum = 0L

        if (totalPopulation != null && totalPopulation.length() > 0) {
          totalPopulationNum = Long.parseLong(totalPopulation.replace(",", ""))
        }
        if(totalYoungPopulationArray.contains(countryName)){
          (countryName, year, totalPopulationNum)
        }else{
          (EMPTY_STRING, year, totalPopulationNum)
        }
        
      }
    }
    
    val resultRDD = yearlyDistributionRDD.filter(rec => { !rec._1.equals(EMPTY_STRING)})
    
    resultRDD.saveAsTextFile(args(1))
    
    session.stop

  }
}

//bin/spark-submit --class com.spark.wba.YoungCountry /home/barun/SparkWorkshopArtifacts/WorldBank/sparkWBDataAnalyserJob.jar /home/barun/SparkWorkshopArtifacts/WorldBank/World_Bank_Indicators.csv /home/barun/SparkWorkshopArtifacts/WorldBank/YoungCountry-out-001