package com.ilovedatajjia
package server

import cats.effect.IO
import org.apache.spark.sql.SparkSession

/**
 * Spark single node server.
 */
object SparkServer {

  // Variable to use for Spark API
  private var sparkInitialization: SparkSession = _
  lazy val spark: SparkSession                  = sparkInitialization // Scala `var` cannot be used with Spark implicits

  /**
   * Run the Spark single node service.
   */
  def run: IO[Unit] = IO {
    sparkInitialization = SparkSession
      .builder()
      .appName("")
      .master("local[*]")
      .config("spark.scheduler.mode", "FAIR")
      .getOrCreate()
  }

}