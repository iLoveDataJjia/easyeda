package com.ilovedatajjia
package api.helpers

import api.helpers.BackendException.AppException
import cats.effect.IO
import cats.effect.implicits._
import cats.implicits._
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import scala.util.Random

/**
 * Utils for [[String]].
 * @note
 *   See 32-126 ASCII characters [[https://en.wikipedia.org/wiki/ASCII#Printable_characters here]].
 */
object StringUtils {

  /**
   * Generate a random string according a given length and characters.
   * @param length
   *   String length
   * @param chars
   *   Characters possible (default are printable characters ASCII 32 to 126, except the character `"`)
   * @return
   *   Generated [[String]]
   */
  def genString(length: Int,
                chars: String =
                  " !#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[]^_`abcdefghijklmnopqrstuvwxyz{|}~")
      : IO[String] = for {
    random <- IO(new Random())
    idx     = (1 to length).map(_ => random.between(0, chars.length))
    out     = idx.foldLeft("")((acc, i) => acc ++ chars(i).toString)
  } yield out

  /**
   * Rich for [[String]].
   * @param x
   *   Applied on
   */
  implicit class StringExtensionRichString(x: String) {

    /**
     * Camel case [[String]] to snake case
     * @return
     *   Snake case version
     */
    def toSnakeCase: String = {
      val pattern = "([a-z])([A-Z])".r
      pattern.replaceAllIn(x, "$1_$2").toLowerCase()
    }

    /**
     * Validate email format. (RFC 5322 official format)
     * @return
     *   Nothing OR
     *   - [[AppException]] if non valid email
     */
    def isValidEmail: IO[Unit] = IO.raiseUnless(
      "^((?:[A-Za-z0-9!#$%&'*+\\-/=?^_`{|}~]|(?<=^|\\.)\"|\"(?=$|\\.|@)|(?<=\".*)[ .](?=.*\")|(?<!\\.)\\.){1,64})(@)([A-Za-z0-9.\\-]*[A-Za-z0-9]\\.[A-Za-z0-9]{2,})$".r
        .matches(x))(AppException("Email format invalid."))

    /**
     * Validate password format requirements.
     *   - 8 to 32 ASCII printable characters (32-126)
     *   - One uppercase letter
     *   - One lowercase letter
     *   - One number character
     *   - One special character
     *
     * @return
     *   Nothing OR
     *   - [[AppException]] if non valid password
     */
    def isValidPwd: IO[Unit] = IO.raiseUnless(
      "^[\\x20-\\x7E]{8,32}$".r.matches(x) && "[A-Z]+".r.findFirstMatchIn(x).isDefined && "[a-z]+".r
        .findFirstMatchIn(x)
        .isDefined && "[0-9]+".r.findFirstMatchIn(x).isDefined && "[^A-Za-z0-9]+".r
        .findFirstMatchIn(x)
        .isDefined)(AppException(
      "Password must contains 8 to 32 characters, an uppercase and lowercase letter, a number and a special character."))

    /**
     * Application entity name convention.
     * @return
     *   Nothing OR
     *   - [[AppException]] if non valid name
     */
    def isValidName: IO[Unit] = IO.raiseUnless("[a-zA-Z0-9]{2,32}".r.matches(x))(
      AppException("Name must contains 2 to 32 alphanumerical characters."))

    /**
     * Convert [[x]] to hashed with SHA3-512.
     * @return
     *   Hashed of [[x]]
     * @note
     *   Can be used for hashing password but the right way is to use a slower and more computing intensive algorithm
     *   like the latest state-of-the art [[https://github.com/phxql/argon2-jvm Argon2]].
     */
    def toSHA3_512: IO[String] = for {
      encoder <- IO(MessageDigest.getInstance("SHA3-512"))
      encoded <- IO(encoder.digest(x.getBytes(StandardCharsets.UTF_8)))
      out     <- encoded.toList.parTraverse(x => IO(String.format("%02x", Byte.box(x)))).map(_.mkString)
    } yield out

    /**
     * Verify [[x]] against a hash.
     * @param hash
     *   Hash
     * @return
     *   [[x]] hashed equal to the provided hash
     */
    def eqSHA3_512(hash: String): IO[Boolean] = x.toSHA3_512.map(_ == hash)

  }

}
