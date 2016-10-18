package api.service
package json

import cats.data.Xor._
import io.circe._
import io.circe.generic.semiauto._
import io.circe.parser._
import io.circe.syntax._

/**
  * https://travisbrown.github.io/circe/tut/codec.html
  */
object JsonSupport extends JsonSupport

trait JsonSupport {

  implicit val prefsDecoder: Decoder[Preferences] = deriveDecoder
  implicit val prefsEncoder: Encoder[Preferences] = deriveEncoder

  def toJson[A: Encoder](obj: A): Json = obj.asJson

  def toJsonString[A: Encoder](obj: A) = toJson(obj).noSpaces

  def parseOrThrow(jsonString: String): Json = {
    parse(jsonString) match {
      case Left(err: ParsingFailure) => throw err
      case Right(json) => json
    }
  }

  def parseOrThrowAs[A: Decoder](jsonString: String): A = {
    parseOrThrow(jsonString).as[A] match {
      case Left(err) => throw err
      case Right(prefs) => prefs
    }
  }

}
