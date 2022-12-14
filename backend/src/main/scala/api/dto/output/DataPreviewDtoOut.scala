package com.ilovedatajjia
package api.dto.output

import DataPreviewDtoOut._
import api.helpers.NormTypeEnum.NormType
import cats.effect.IO
import io.circe.Decoder
import io.circe.Encoder
import io.circe.generic.semiauto._
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

/**
 * For JSON automatic derivation.
 * @param dataSchema
 *   Schema
 * @param dataValues
 *   Values
 */
case class DataPreviewDtoOut(dataConf: DataConf, dataSchema: List[DataSchema], dataValues: List[List[String]])

/**
 * [[DataPreviewDtoOut]] companion object with encoders & decoders.
 */
object DataPreviewDtoOut {

  // JSON (de)serializers
  implicit val dataConfEnc: Encoder[DataConf]          = deriveEncoder
  implicit val dataConfDec: Decoder[DataConf]          = deriveDecoder
  implicit val dataSchEnc: Encoder[DataSchema]         = deriveEncoder
  implicit val dataSchDec: Decoder[DataSchema]         = deriveDecoder
  implicit val dataPrevEnc: Encoder[DataPreviewDtoOut] = deriveEncoder
  implicit val dataPrevDec: Decoder[DataPreviewDtoOut] = deriveDecoder

  // Entity encoder
  implicit val dataConfEntEnc: EntityEncoder[IO, DataConf]          = jsonEncoderOf[IO, DataConf]
  implicit val dataSchEntEnc: EntityEncoder[IO, DataSchema]         = jsonEncoderOf[IO, DataSchema]
  implicit val dataPrevEntEnc: EntityEncoder[IO, DataPreviewDtoOut] = jsonEncoderOf[IO, DataPreviewDtoOut]

  /**
   * For preview configurations.
   * @param nbRows
   *   Number of rows
   * @param nbCols
   *   Number of columns
   */
  case class DataConf(nbRows: Int, nbCols: Int)

  /**
   * For schema representation.
   * @param colName
   *   Column name
   * @param colType
   *   Column type
   */
  case class DataSchema(colName: String, colType: NormType)

}
