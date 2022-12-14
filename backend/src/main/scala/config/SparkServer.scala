package com.ilovedatajjia
package config

import cats.effect.IO
import config.ConfigLoader.sparkUIPort
import org.apache.spark.sql.SparkSession

/**
 * Spark single node server.
 */
object SparkServer {

  // Variable to use for Spark API
  private var sparkInitialization: SparkSession = _
  implicit lazy val spark: SparkSession         = sparkInitialization // Scala `var` cannot be used with Spark implicits

  /**
   * Run the Spark single node service.
   */
  def run: IO[Unit] = IO {
    sparkInitialization = SparkSession
      .builder()
      .appName("EasyEDA")
      .master("local[*]")
      .config("spark.ui.port", sparkUIPort)
      .config("spark.scheduler.mode", "FAIR")
      .config("spark.scheduler.allocation.file", getClass.getResource("/spark/fairscheduler.xml").getPath)
      .config("spark.scheduler.pool", "fairPool")        // Pool name defined in the XML file
      .getOrCreate()
    sparkInitialization.sparkContext.setLogLevel("WARN") // Remove all INFO & DEBUG logs
  }

}
