package mongo

import api.service.json.JsonSupport
import com.mongodb.casbah.Imports._
import io.circe.{Json, Encoder}

import scala.reflect.ClassTag

/**
  * See http://mongodb.github.io/casbah/3.1/getting-started/
  *
  * see
  * http://mongodb.github.io/mongo-scala-driver/1.1/getting-started/quick-tour/
  * https://github.com/mongodb/mongo-scala-driver/blob/master/examples/src/test/scala/tour/Helpers.scala
  */
object Implicits {

  implicit class ClientOps(val client: MongoClient) extends AnyVal {
    def openDatabase(name: String): MongoOps = client(name)
  }


  implicit class CollectionOps(val col: MongoCollection) extends AnyVal {
    def upsert[A : Encoder](value : A) = {
      import JsonSupport._
      val json: Json = toJson(value)
      val obj = MongoDBObject()
    }

  }

  implicit class MongoOps(val db: MongoDB) extends AnyVal {
    def collectionName[A: ClassTag] = implicitly[ClassTag[A]].runtimeClass.getSimpleName.filter(_.isLetterOrDigit).toLowerCase

    def collection[A: ClassTag]: CollectionOps = db(collectionName)
  }

}
