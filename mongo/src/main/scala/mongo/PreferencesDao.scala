package mongo

trait PreferencesDao {

}

object PreferencesDao {
  import com.mongodb.casbah.Imports._

  val DefaultPort = 27017

  def apply(host : String, port : Int, database : String): PreferencesDao = {
    val mongoClient: MongoClient = MongoClient(host, port)
    val db = mongoClient.apply(database)
    db.co
  }
}
