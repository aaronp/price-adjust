package mongo

import api.service.json.JsonSupport
import com.typesafe.scalalogging.StrictLogging
import io.circe.Decoder
import org.mongodb.scala._

trait Unmarshaller[T] {
  def unmarshal(obj : Document) : T
}

object Unmarshaller {
  class JsonUnmarshaller[T](implicit decoder : Decoder[T]) extends Unmarshaller[T] with JsonSupport with StrictLogging {
    override def unmarshal(obj : Document) : T = {
      val json = obj.toString
      logger.info(json)
      parseOrThrowAs[T](json)
    }
  }
  object Implicits {
    implicit def asUnmarshaller[T : Decoder] : Unmarshaller[T] = {
      new JsonUnmarshaller[T]
    }
  }
}