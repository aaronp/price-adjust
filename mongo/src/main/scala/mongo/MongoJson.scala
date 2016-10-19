package mongo

import io.circe.Json
import org.mongodb.scala.Document

object MongoJson {

  /*
   * TODO - instead of going Json => String => Document, parse directly
   */
  def dbObjectFromJsonString(json: String) = {
    val obj: Document = Document(json)
    if (!obj.contains("_id")) {
      val updated = obj.get("id").map { id =>
        obj.updated("_id", id)
      }
      updated.getOrElse(sys.error(s"no 'id' or '_id' field found on $json"))
    } else {
      obj
    }
  }

  def dbObjectFromJson(json: Json): Document = {
    dbObjectFromJsonString(json.toString())
    //    json.fold(
    //      Xor.Left("json wasn't an array or document: $json"),
    //      (b : Boolean) => Document("boolean" -> b),
    //      (b : JsonNumber) => Document("boolean" -> b),
    //      (b : String) => Document("boolean" -> b),
    //      (b : List[Json]) => Document("boolean" -> b),
    //      (b : JsonObject) => Document("boolean" -> b)
    //
    //    )
  }
}
