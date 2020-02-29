package com.spark.wba

import org.apache.spark.sql.SparkSession
import java.lang.Long
import scala.tools.scalap.scalax.util.StringUtil

object HighestUrbanPopulation {

  def main(args: Array[String]) {

    if (args.length < 2) {
      System.err.println("Usages: com.spark.wba.HighestUrbanPopulation <inPath> <outPath>");
      System.exit(1);
    }

    val session = SparkSession.builder().appName("HighestUrbanPopulation").getOrCreate();

    val wbDataRDD = session.read.textFile(args(0)).rdd

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
        if (recordSplits.length == 20) {
          if (year.contains("2010")) {

            try {
              val copyurbanPopulation = recordSplits(10)
            } catch {
              case ex: ArrayIndexOutOfBoundsException =>
                {
                  println("Failed for country:" + countryName)
                  (countryName, Long.parseLong("0"))
                }
                (countryName, Long.parseLong("0"))
            }
            val urbanPopulation = recordSplits(10)
            if (urbanPopulation == null || urbanPopulation.equals("")) {
              (countryName, Long.parseLong("0"))
            } else {
              // urban population is in form of "244,074"
              val numberFormatUrbanPopulation = urbanPopulation.replace(",", "").replace("\"", "");

              (countryName, Long.parseLong(numberFormatUrbanPopulation))
            }

          } else {
            // Not counting stale population metrics
            (countryName, Long.parseLong("0"))
          }

        } else {
          (countryName, Long.parseLong("0"))
        }

      }
    }.reduceByKey(_ + _)

    // Sorting the map by its population
    val sortedOnPopulation = countryVsUrbanPopulation.sortBy(_._2, false)

    // Fetching first entry from map
    val contryHavingHighestPopulation = sortedOnPopulation.zipWithIndex.filter(_._2 < 1)
    contryHavingHighestPopulation.saveAsTextFile(args(1))

    session.stop

  }
}

//bin/spark-submit --class com.spark.wba.HighestUrbanPopulation /home/barun/SparkWorkshopArtifacts/WorldBank/sparkWBDataAnalyserJob.jar /home/barun/SparkWorkshopArtifacts/WorldBank/World_Bank_Indicators.csv /home/barun/SparkWorkshopArtifacts/WorldBank/wb-out-001