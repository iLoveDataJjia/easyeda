package com.ilovedatajjia
package api.routes

import api.controllers.UserCtrl
import api.dto.input._
import api.dto.output._
import api.helpers.BackendException
import api.helpers.BackendException._
import api.models.UserMod
import api.services.UserSvc
import cats.effect.IO
import cats.implicits._
import org.http4s.HttpRoutes
import sttp.tapir._
import sttp.tapir.json.circe._
import sttp.tapir.server.PartialServerEndpoint
import sttp.tapir.server.http4s.Http4sServerInterpreter

/**
 * Routes for users management.
 */
object UserRts extends GenericRts {

  // Create user
  private val createEpt: PublicEndpoint[UserFormIDto, BackendException, UserStatusODto, Any] = ept
    .summary("create user account")
    .post
    .in("user" / "create")
    .in(jsonBody[UserFormIDto])
    .out(jsonBody[UserStatusODto])
  private val createRts: HttpRoutes[IO]                                                      =
    Http4sServerInterpreter[IO]().toRoutes(createEpt.serverLogic(UserCtrl.impl.createUser(_).toErrHandled))

  // Login user
  private val loginEpt: PublicEndpoint[LoginFormIDto, BackendException, TokensODto, Any] = ept
    .summary("login user account")
    .post
    .in("user" / "login")
    .in(jsonBody[LoginFormIDto])
    .out(jsonBody[TokensODto])
  private val loginRts: HttpRoutes[IO]                                                   =
    Http4sServerInterpreter[IO]().toRoutes(loginEpt.serverLogic(UserSvc.impl.loginUser(_).toErrHandled))

  // Refresh user token
  private val refreshEpt: PublicEndpoint[String, BackendException, TokensODto, Any] = ept
    .summary("refresh user tokens")
    .post
    .in("user" / "refresh")
    .in(header[String]("DataPiU-Refresh-Token"))
    .out(jsonBody[TokensODto])
  private val refreshRts: HttpRoutes[IO]                                            =
    Http4sServerInterpreter[IO]().toRoutes(refreshEpt.serverLogic(UserSvc.impl.grantTokens(_).toErrHandled))

  // Retrieve user status
  private val getEpt: PartialServerEndpoint[String, UserMod, Unit, BackendException, UserStatusODto, Any, IO] = authEpt
    .summary("retrieve user status")
    .get
    .in("user" / "status")
    .out(jsonBody[UserStatusODto])
  private val getRts: HttpRoutes[IO]                                                                          =
    Http4sServerInterpreter[IO]().toRoutes(getEpt.serverLogic(user => _ => UserSvc.impl.toDto(user).toErrHandled))

  /**
   * Get all endpoints.
   * @return
   *   Concatenated endpoints
   */
  override def docEpt: List[AnyEndpoint] = List(createEpt, loginEpt, refreshEpt) ++ List(getEpt).map(_.endpoint)

  /**
   * Get all applicative routes.
   * @return
   *   Concatenated routes
   */
  override def appRts: HttpRoutes[IO] = createRts <+> loginRts <+> refreshRts <+> getRts

}
