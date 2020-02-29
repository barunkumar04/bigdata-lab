package com.spark.tda

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.types.StringType
import com.spark.tda.schema.Traveller
import org.apache.spark.sql.Encoders
import org.apache.spark.sql.Row

// Find the average age of people who died and who survived
object KPI1_AvgAgeOfPeople {

  def main(args: Array[String]) {

    //Validate input parameter
    if (args.length > 1) {
      System.err.println("Usages: KPI1_AvgAgeOfPeople <inPath>")
      System.exit(1)
    }

    //Start spark session
    val sparkSession = SparkSession.builder().appName("KPI1_AvgAgeOfPeople").getOrCreate();

    //Read file and initiate RDD
    val recordRDD = sparkSession.read.csv(args(0)).rdd

    //Record: "1","1st",1,"Allen, Miss Elisabeth Walton",29.0000,"Southampton","St Louis, MO","B-5","24160 L221","2","female"

    val parsedRDD = recordRDD.map(row => {

      val row_name = row.getString(0).replace("\"", "");
      val pclass = row.getString(1).replace("\"", "");
      val survived = row.getInt(2);
      val name = row.getString(3).replace("\"", "");
      val age = row.getInt(4);
      val embarked = row.getString(5).replace("\"", "");
      val home_dest = row.getString(6).replace("\"", "");
      val room = row.getString(7).replace("\"", "");
      val ticket = row.getString(8).replace("\"", "");
      val boat = row.getString(9).replace("\"", "");
      val sex = row.getString(10).replace("\"", "");

      Row(survived, age)
      
    })

     //Creating schema of the recordRDD
    val schema = new StructType()
      .add(StructField("survived", IntegerType, true))
      .add(StructField("age", IntegerType, true))

    case class Traveller1 (survived : String, age: Int)
    
    val df = sparkSession.createDataFrame(parsedRDD, schema)
    
    
    //df.as(Encoders.bean(Traveller.class)
    
//    val encoder = org.apache.spark.sql.Encoders.product[Traveller.class]
    
  //  val ds = df.as(Encoders.bean(Traveller.class))
    // Encoders are created for case classes


    
  }

}