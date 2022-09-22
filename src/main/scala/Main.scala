package com.ilovedatajjia

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import services.AppServer
import services.SparkServer

/**
 * Application entrypoint.
 */
object Main extends IOApp {

  /**
   * Run all the required services and server.
   * @param args
   *   Entrypoint arguments
   * @return
   *   Exit code
   */
  override def run(args: List[String]): IO[ExitCode] =
    // SparkServer.run >>
    AppServer.run

}
