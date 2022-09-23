package com.ilovedatajjia
package routes.job

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import controllers.JobController
import io.circe.Json
import models.session.Session
import org.http4s._
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.dsl.io._
import routes.utils.Auth._
import routes.utils.Request._
import routes.utils.Response._

/**
 * Routes related to CSV files management.
 */
object JobRoutes {

  // Define preview route
  private val previewRoute: AuthedRoutes[Session, IO] = AuthedRoutes.of {
    case req @ POST -> Root / "preview" as session =>
      // Request with file upload and its parameters
      req.req.withJSONAndFileBytesMultipart("sparkArgs", "fileBytes") { (sparkArgs: Json, fileStr: String) =>
        // println("############## sparkArgsDrained")
        // println(sparkArgsDrained.unsafeRunSync())
        // println("############## fileStrDrained")
        // println(fileStrDrained.unsafeRunSync())

        JobController.computePreview(session, sparkArgs, fileStr).toResponse
      }
  }

  // Merge all routes
  val routes: HttpRoutes[IO] = withAuth(previewRoute) // Always the non-auth routes first

}
