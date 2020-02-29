package com.spark.wba

import org.apache.spark.sql.SparkSession
import java.lang.Long
import scala.tools.scalap.scalax.util.StringUtil

object MostPopulousCountries2 {

  def main(args: Array[String]) {

    if (args.length < 2) {
      System.err.println("Usages: com.spark.wba.HighestUrbanPopulation <inPath> <outPath>");
      System.exit(1);
    }

    val session = SparkSession.builder().appName("HighestUrbanPopulation").getOrCreate();

    val wbDataRDD = session.read.csv(args(0)).rdd

    val countryVsUrbanPopulation = wbDataRDD.map { record =>
      {
        // "Bahamas, The",7/1/2000,,,"31,524",8,17,"1,072",6,"297,651","244,074",18,74,69,72,29,65,5,"6,327,552,000","21,258"

        val countryName = record.getString(0)
        val year = record.getString(1)
        var populationNum = 0L
        val population = record.getString(9)

        if (population.length() > 0) {
          populationNum = Long.parseLong(population.replace(",", ""))
        }

        (populationNum, countryName)
      }
    }
    .groupByKey() // [India, [12345, 3434343, 23232432...., 23242]] 
    .map( rec => {
      (rec._1, rec._2.max)
    })
    .sortByKey(false)
    .take(10)

    session.sparkContext.parallelize(Seq(countryVsUrbanPopulation)(0), 1).saveAsTextFile(args(1))

    session.stop

  }
}

//bin/spark-submit --class com.spark.wba.MostPopulousCountries2 /home/barun/SparkWorkshopArtifacts/WorldBank/sparkWBDataAnalyserJob.jar /home/barun/SparkWorkshopArtifacts/WorldBank/World_Bank_Indicators.csv /home/barun/SparkWorkshopArtifacts/WorldBank/MostPopulousCountries-out-001