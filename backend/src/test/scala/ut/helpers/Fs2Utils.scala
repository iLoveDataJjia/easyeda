package com.ilovedatajjia
package ut.helpers

import cats.data.EitherT
import cats.effect.IO
import cats.implicits._
import fs2._
import fs2.io.file._
import java.nio.file.Paths

/**
 * Static function for building [[fs2]] objects.
 */
object Fs2Utils {

  /**
   * Read simulated [[Stream]] representation.
   * @param path
   *   The [[Stream]] resource path
   * @param nbChunks
   *   Simulated number of chunks in the stream (Not guaranteed result if not respecting `>= 1`)
   * @return
   *   Simulated [[Stream]] OR
   *   - [[Throwable]] if issue occurred
   */
  def fromResourceStream(path: String, nbChunks: Int): EitherT[IO, Throwable, Stream[IO, Byte]] = for {
    _             <- IO(nbChunks.ensuring(_ >= 1)).attemptT
    stringRep     <- Files[IO]
                       .readAll(Path(Paths.get(getClass.getResource(path).toURI).toString))
                       .through(text.utf8.decode)
                       .compile
                       .string
                       .attemptT
    stringRepLen   = stringRep.length
    winSize        = if (stringRepLen % nbChunks == 0) stringRepLen / nbChunks
                     else stringRepLen / nbChunks + 1 // Round up equivalent
    stringSplitRep = stringRep.grouped(winSize).toList
    streamRep      = Stream.emits(stringSplitRep).through(text.utf8.encode)
  } yield streamRep

}
