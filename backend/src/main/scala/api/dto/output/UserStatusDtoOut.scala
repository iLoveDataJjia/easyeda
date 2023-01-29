package com.ilovedatajjia
package api.dto.output

import api.helpers.CirceUtils._ // Needed import
import api.helpers.TapirUtils._ // Needed import
import io.circe._
import io.circe.generic.semiauto._
import java.sql.Timestamp
import sttp.tapir.Schema

/**
 * User status.
 * @param id
 *   User id
 * @param email
 *   E-mail unique identifier of the account
 * @param username
 *   Name displayed on the account
 * @param createdAt
 *   Account created at
 * @param validatedAt
 *   Account email validated at
 * @param updatedAt
 *   Account updated at
 * @param activeAt
 *   Account latest activity at
 */
case class UserStatusDtoOut(id: Long,
                            email: String,
                            username: String,
                            createdAt: Timestamp,
                            validatedAt: Option[Timestamp],
                            updatedAt: Timestamp,
                            activeAt: Timestamp)

/**
 * [[UserStatusDtoOut]] companion.
 */
object UserStatusDtoOut {

  // JSON (de)serializers
  implicit val enc: Encoder[UserStatusDtoOut] = deriveEncoder
  implicit val dec: Decoder[UserStatusDtoOut] = deriveDecoder

  // Schema serializer
  implicit val sch: Schema[UserStatusDtoOut] = Schema.derived

}
