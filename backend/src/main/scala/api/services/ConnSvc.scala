package com.ilovedatajjia
package api.services

import api.dto.input.ConnFormIDto
import api.dto.input.ConnFormIDto._
import api.dto.output._
import api.helpers._
import api.helpers.BackendException.AppException
import api.helpers.DoobieUtils._
import api.models._
import cats.effect.IO
import cats.implicits._

/**
 * Service layer for connection.
 */
trait ConnSvc {

  /**
   * Test connection.
   * @param form
   *   Provided connection form
   * @return
   *   [[ConnTestODto]]
   */
  def testConn(form: ConnFormIDto): IO[ConnTestODto] = (form match {
    case PostgresFormIDto(_, host, port, dbName, user, pwd)        =>
      JdbcUtils.testConn("org.postgresql.Driver",
                         s"jdbc:postgresql://$host:$port/$dbName",
                         "user"     -> user,
                         "password" -> pwd)
    case MongoFormIDto(_, hostPort, dbAuth, replicaSet, user, pwd) =>
      MongoUtils.testConn(hostPort.map(x => x.host -> x.port), dbAuth, replicaSet, user, pwd)
  }).map { case (isUp, errMsg) => ConnTestODto(isUp, errMsg) }

  /**
   * Create connection.
   * @param user
   *   User
   * @param form
   *   Provided connection form
   * @return
   *   All [[ConnODto]]
   */
  def createConn(user: UserMod, form: ConnFormIDto)(implicit connModDB: ConnMod.DB): IO[ConnODto] = for {
    connMod <- connModDB(user.id, form)
  } yield ConnODto(connMod.id, connMod.`type`, connMod.name)

  /**
   * List all connections.
   * @param user
   *   User
   * @return
   *   All connections
   */
  def listConn(user: UserMod)(implicit connModDB: ConnMod.DB): IO[List[ConnODto]] = for {
    allConn <- connModDB.select(fr"user_id = ${user.id}")
    allDto  <- allConn.traverse { x => IO(ConnODto(x.id, x.`type`, x.name)) }
  } yield allDto

  /**
   * Grant connection if user has authorization.
   * @param user
   *   User
   * @param connId
   *   Connection to grant id
   * @return
   *   Granted connection
   */
  def grantConn(user: UserMod, connId: Long)(implicit connModDB: ConnMod.DB): IO[ConnMod] = for {
    allConn <- connModDB.select(fr"user_id = ${user.id} AND id = $connId")
    conn    <- allConn match {
                 case List(conn) => IO(conn)
                 case List()     => IO.raiseError(AppException("Forbidden connection access or no connection retrievable."))
                 case _          =>
                   IO.raiseError(
                     AppException(s"Corrupted table, incoherent amount (=${allConn.length}) of connections retrieved."))
               }
  } yield conn

  /**
   * Test known connection.
   * @param user
   *   User
   * @param connId
   *   Connection id
   * @return
   *   [[ConnTestODto]] OR
   *   - [[AppException]] if forbidden access
   */
  def testKnownConn(user: UserMod, connId: Long)(implicit connModDB: ConnMod.DB): IO[ConnTestODto] = for {
    conn <- grantConn(user, connId)
    dto  <- conn.testConn
  } yield dto

}

/**
 * Auto-DI on import.
 */
object ConnSvc { implicit val impl: ConnSvc = new ConnSvc {} }
