package mongo

import api.service.json.JsonSupport
import com.mongodb.DBObject
import com.typesafe.scalalogging.StrictLogging
import io.circe.Decoder

trait Unmarshaller[T] {
  def unmarshal(obj : DBObject) : T
}

object Unmarshaller {
  class JsonUnmarshaller[T](implicit decoder : Decoder[T]) extends Unmarshaller[T] with JsonSupport with StrictLogging {
    override def unmarshal(obj : DBObject) : T = {
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