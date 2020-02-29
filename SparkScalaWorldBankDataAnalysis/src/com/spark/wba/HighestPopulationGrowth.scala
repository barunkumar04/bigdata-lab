package com.spark.wba

import org.apache.spark.sql.SparkSession
import java.lang.Long
import scala.tools.scalap.scalax.util.StringUtil
import scala.collection.mutable.HashMap
import com.spark.wba.model.WorldBankDataModel
import com.spark.wba.model.WorldBankDataModel
import com.spark.wba.model.WorldBankDataModel
import com.spark.wba.model.WorldBankDataModel
import org.apache.spark.rdd.RDD
import com.spark.wba.model.WorldBankDataModel

// Growth rate calculation formula: https://pages.uoregon.edu/rgp/PPPM613/class8a.htm
object HighestPopulationGrowth {

  def main(args: Array[String]) {

    if (args.length < 2) {
      System.err.println("Usages: com.spark.wba.HighestUrbanPopulation <inPath> <outPath>");
      System.exit(1);
    }
    val WB_DATA_REGEX = "(?x)   " +
      ",          " + // Split on comma
      "(?=        " + // Followed by
      "  (?:      " + // Start a non-capture group
      "    [^\"]* " + // 0 or more non-quote characters
      "    \"     " + // 1 quote
      "    [^\"]* " + // 0 or more non-quote characters
      "    \"     " + // 1 quote
      "  )*       " + // 0 or more repetition of non-capture group (multiple of 2 quotes will be even)
      "  [^\"]*   " + // Finally 0 or more non-quotes
      "  $        " + // Till the end  (This is necessary, else every comma will satisfy the condition)
      ")          "

    val session = SparkSession.builder().appName("HighestUrbanPopulation").getOrCreate();

    val wbDataRDD = session.read.textFile(args(0)).rdd

    var countryWisePopulations = new HashMap[String, WorldBankDataModel].withDefaultValue(null)

    val countryVsUrbanPopulation = wbDataRDD.map { record =>
      {
        // "Bahamas, The",7/1/2000,,,"31,524",8,17,"1,072",6,"297,651","244,074",18,74,69,72,29,65,5,"6,327,552,000","21,258"

        val recordSplits = record.trim().split("(?x)   " +
          ",          " + // Split on comma
          "(?=        " + // Followed by
          "  (?:      " + // Start a non-capture group
          "    [^\"]* " + // 0 or more non-quote characters
          "    \"     " + // 1 quote
          "    [^\"]* " + // 0 or more non-quote characters
          "    \"     " + // 1 quote
          "  )*       " + // 0 or more repetition of non-capture group (multiple of 2 quotes will be even)
          "  [^\"]*   " + // Finally 0 or more non-quotes
          "  $        " + // Till the end  (This is necessary, else every comma will satisfy the condition)
          ")          ") // End look-ahead

        val countryName = recordSplits(0)
        val year = recordSplits(1)
        
        val population = recordSplits(9)
        val numberFormatPopulation = population.replace(",", "").replace("\"", "")

        var worldBankDataModel = countryWisePopulations(countryName)

        if (worldBankDataModel == null) {
          worldBankDataModel = new WorldBankDataModel()

        }

        if (year.contains("2000")) {
          worldBankDataModel.pastPopulation = Long.valueOf(numberFormatPopulation)
        }

        if (year.contains("2010")) {
          worldBankDataModel.currentPopulation = Long.valueOf(numberFormatPopulation)
        }

        countryWisePopulations.put(countryName, worldBankDataModel)
        //System.out.println("Debug in mapper: "+countryWisePopulations)
    
      }
    }

    //To invoke map. TODO - another way??
    countryVsUrbanPopulation.count()
    
   // System.out.println("Debug: "+countryWisePopulations)
    
    var isFirst = true
    var maxGrowthPercentage = Long.MIN_VALUE
    var countryNameMaxPopulation = ""
    System.out.println("Debug: going to in FOREACH LOOP")
    
    // Growth calculation
    countryWisePopulations.foreach{ case (key: String, value: WorldBankDataModel) => {
      System.out.println("Debug: in FOREACH LOOP")
      
      value.growthPercentage = ((value.currentPopulation - value.pastPopulation) / value.pastPopulation) * 100

      if (isFirst) {
        countryNameMaxPopulation = key
        maxGrowthPercentage = value.growthPercentage
        isFirst = false
        System.out.println("Debug: This is first k-v")
        System.out.println("Debug: countryNameMaxPopulation - maxGrowthPercentage"+ countryNameMaxPopulation +"-"+maxGrowthPercentage)
      }

      if (value.growthPercentage > maxGrowthPercentage) {
        countryNameMaxPopulation = key
        maxGrowthPercentage = value.growthPercentage
        System.out.println("Debug: Next max countryNameMaxPopulation - maxGrowthPercentage"+ countryNameMaxPopulation +"-"+maxGrowthPercentage)
      }

    }}

    val highestPopulationRDD = session.sparkContext.parallelize(Seq(countryNameMaxPopulation +","+maxGrowthPercentage))
    
    highestPopulationRDD.saveAsTextFile(args(1))
    
    session.stop

  }
}

//bin/spark-submit --class com.spark.wba.HighestPopulationGrowth /home/barun/SparkWorkshopArtifacts/WorldBank/sparkWBDataAnalyserJob.jar /home/barun/SparkWorkshopArtifacts/WorldBank/World_Bank_Indicators.csv /home/barun/SparkWorkshopArtifacts/WorldBank/HighestPopulationGrowth-out-001