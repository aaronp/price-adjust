package mongo

import com.mongodb.{BasicDBObject, DBObject}
import io.circe.Json

object MongoJson {

  /*
   * TODO - instead of going Json => String => Document, parse directly
   */
  def dbObjectFromJsonString(json: String) = {
    val obj = BasicDBObject.parse(json)
    if (!obj.containsField("_id")) {
      require(obj.containsField("id"), s"no 'id' or '_id' field found on $json")
      val id: AnyRef = obj.get("id")
      obj.put("_id", id)
      obj.remove("id")
    }
    obj
  }

  def dbObjectFromJson(json: Json): DBObject = {
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
