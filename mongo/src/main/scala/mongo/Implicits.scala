package mongo

import api.service.json.JsonSupport
import com.mongodb.{DBCollection, DBCursor, DBObject}
import io.circe.{Encoder, Json}

import org.mongodb.scala.MongoClient
import scala.collection.JavaConverters
import scala.reflect.ClassTag

/**
  * See http://mongodb.github.io/casbah/3.1/getting-started/
  *
  * see
  * http://mongodb.github.io/mongo-scala-driver/1.1/getting-started/quick-tour/
  * https://github.com/mongodb/mongo-scala-driver/blob/master/examples/src/test/scala/tour/Helpers.scala
  */
object Implicits {

  implicit class RichString(val host: String) extends AnyVal {

    def withPort(port: Int) = {
      val mongoClient = MongoClient(host, port)
      new RichClient(mongoClient)
    }
  }

  implicit class RichClient(val mongoClient: MongoClient) extends AnyVal {
    def db(name: String) = new RichDB(mongoClient.getDB(name))

    def dbNames = {
      mongoClient.getDatabaseNames.toSet
    }
  }

  implicit class RichDB(val mongo: MongoDB) extends AnyVal {
    def collection[T: ClassTag]: RichCollection = collection(collectionName[T])

    def collection(name: String): RichCollection = mongo.getCollection(name)

    def collections = mongo.getCollectionNames.toSet
  }

  implicit class RichCollection(val collection: DBCollection) extends AnyVal {

    def find[T <% DBObject, R: Unmarshaller](criteria: T) = {
      val found: DBCursor = collection.find(criteria)
      val objs = JavaConverters.iterableAsScalaIterableConverter(found).asScala
      val u = implicitly[Unmarshaller[R]]
      objs.map(u.unmarshal)
    }

    def insert[T <% DBObject](doc: T) = {
      collection.insert(doc)
    }
  }

  implicit def stringToDBObj(json: String) = MongoJson.dbObjectFromJsonString(json)

  implicit def jsonToDBObj(json: Json) = MongoJson.dbObjectFromJson(json)

  implicit def objToDBObj[T: Encoder](obj: T) = jsonToDBObj(JsonSupport.toJson(obj))

  def collectionName[T: ClassTag] = implicitly[ClassTag[T]].runtimeClass.getSimpleName.filter(_.isLetterOrDigit).toLowerCase()
}
