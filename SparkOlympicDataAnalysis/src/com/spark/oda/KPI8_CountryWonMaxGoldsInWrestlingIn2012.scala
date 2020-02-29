package com.spark.oda

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.Row
import org.apache.spark.sql.{ Row, SparkSession, types }

//Country won highest no of medals in wrestling in 2012
object KPI8_CountryWonMaxGoldsInWrestlingIn2012 {

  def main(args: Array[String]) = {

    //Validating args

    if (args.length > 1) {
      System.err.print("Usages: KPI8_CountryWonMaxGoldsInWrestlingIn2012 <inputFilePath>")
      System.exit(1)
    }

    //Creating spark session
    val sparkSession = SparkSession.builder().appName("KPI8_CountryWonMaxGoldsInWrestlingIn2012").getOrCreate()

    //Creating record read rdd.
    val rawRecordsRDD = sparkSession.read.csv(args(0)).rdd
    
    val filteredRecordsRDD =  rawRecordsRDD.filter(row => {row.getString(0) != null && row.getString(0).length() > 0})
    .map(row => {
      Row(row(0), //  athlete
        Integer.parseInt(row(1).toString()), // age
        row(2), // Country 
        Integer.parseInt(row(3).toString()), // Year
        row(4), //closingCeremonyDate
        row(5), // Sport
        Integer.parseInt(row(6).toString()), 
        Integer.parseInt(row(7).toString()), 
        Integer.parseInt(row(8).toString()),
        Integer.parseInt(row(9).toString())
      )
    })

    //Creating schema of the recordRDD
    val schema = new StructType()
      .add(StructField("athlete", StringType, false))
      .add(StructField("age", IntegerType, false))
      .add(StructField("country", StringType, false))
      .add(StructField("year", IntegerType, false))
      .add(StructField("closingCeremonyDate", StringType, false))
      .add(StructField("sport", StringType, false))
      .add(StructField("countGoldMedals", IntegerType, false))
      .add(StructField("countSilverMedals", IntegerType, false))
      .add(StructField("countBronzeMedals", IntegerType, false))
      .add(StructField("countTotalMedals", IntegerType, false))

    //Converting RDD to spark dataframe.
    val olympicDF = sparkSession.createDataFrame(filteredRecordsRDD, schema)

    //Simply showing the records read above
    olympicDF.show()
    
    //all records
    /**
        +--------------------+---+-------------+----+-------------------+--------------------+---------------+-----------------+-----------------+----------------+
        |             athlete|age|      country|year|closingCeremonyDate|               sport|countGoldMedals|countSilverMedals|countBronzeMedals|countTotalMedals|
        +--------------------+---+-------------+----+-------------------+--------------------+---------------+-----------------+-----------------+----------------+
        |      Michael Phelps| 23|United States|2008|          8/24/2008|            Swimming|              8|                0|                0|               8|
        |      Michael Phelps| 19|United States|2004|          8/29/2004|            Swimming|              6|                0|                2|               8|
        |    Natalie Coughlin| 25|United States|2008|          8/24/2008|            Swimming|              1|                2|                3|               6|
        |     Allison Schmitt| 22|United States|2012|          8/12/2012|            Swimming|              3|                1|                1|               5|
        |    Natalie Coughlin| 21|United States|2004|          8/29/2004|            Swimming|              2|                2|                1|               5|
        |         Ryan Lochte| 24|United States|2008|          8/24/2008|            Swimming|              2|                0|                2|               4|
        |      Inge de Bruijn| 30|  Netherlands|2004|          8/29/2004|            Swimming|              1|                1|                2|               4|
        +--------------------+---+-------------+----+-------------------+--------------------+---------------+-----------------+-----------------+----------------+
        only showing top 7 rows

     * 
     */
     
    //Creating olympic data view
    olympicDF.createOrReplaceTempView("olympic_data")
    
    //Country won highest no of medals in wrestling in 2012
    sparkSession.sql("select country, year, sport, sum(countTotalMedals) from olympic_data where sport = 'Wrestling' group by country, year, sport order by sum(countTotalMedals) desc").show(1)

    //closing the spark session
    sparkSession.stop()
  }

}

//bin/spark-submit --class com.spark.oda.KPI8_CountryWonMaxGoldsInWrestlingIn2012 /home/barun/SparkWorkshopArtifacts/OlympicData/olympicDataAnalyserJob.jar /home/barun/SparkWorkshopArtifacts/OlympicData/olympic_Data.csv