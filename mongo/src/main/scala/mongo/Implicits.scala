package mongo

import api.service.json.JsonSupport
import io.circe.{Encoder, Json}
import org.mongodb.scala._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
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
      val mongoClient: MongoClient = MongoClient(s"mongodb://$host:$port")
      new RichClient(mongoClient)
    }
  }

  implicit class RichObservable[T](val obs: Observable[T]) extends AnyVal {
    def future: Future[List[T]] = {
      val f = Observables.futureObserver[T]
      obs.subscribe(f)
      f.future
    }
  }

  implicit class RichClient(val mongoClient: MongoClient) extends AnyVal {
    def db(name: String) = new RichDB(mongoClient.getDatabase(name))

    def dbNames = mongoClient.listDatabaseNames().future
  }

  implicit class RichDB(val mongo: MongoDatabase) extends AnyVal {
    def collection[T: ClassTag]: RichCollection = collection(collectionName[T])

    def collection(name: String): RichCollection = {
      mongo.getCollection(name)
    }

    def collections = mongo.listCollectionNames().future
  }

  implicit class RichCollection(val collection: MongoCollection[Document]) extends AnyVal {

    def find[T <% Document, R: Unmarshaller](criteria: T): Future[List[R]] = {
      val found: FindObservable[Document] = collection.find(criteria)
      val u = implicitly[Unmarshaller[R]]
      found.future.map(_.map(u.unmarshal))
    }

    def insert[T <% Document](doc: T): Future[Boolean] = {
      val x: Observable[Completed] = collection.insertOne(doc)
      x.future.map(_ => true)
    }
  }

  implicit def stringToDBObj(json: String) = MongoJson.dbObjectFromJsonString(json)

  implicit def jsonToDBObj(json: Json) = MongoJson.dbObjectFromJson(json)

  implicit def objToDBObj[T: Encoder](obj: T) = jsonToDBObj(JsonSupport.toJson(obj))

  def collectionName[T: ClassTag]: String = {
    val name: String = implicitly[ClassTag[T]].runtimeClass.getSimpleName
    augmentString(name).filter(_.isLetterOrDigit).toLowerCase()
  }
}
