package mongo.preferences

import api.service.{PreferenceKey, PreferenceService, Preferences}
import com.mongodb.DBCollection
import mongo.Implicits
import mongo.Implicits.RichCollection

trait PreferencesDao extends PreferenceService

object PreferencesDao {

  //192.168.99.100:32768
  def apply(host: String, port: Int, database: String): PreferencesDao = {
    import Implicits._
    val coll = host.withPort(port).db(database).collection[Preferences]
    new MongoPreference(coll)
  }

//  def apply(collection: DBCollection) : PreferencesDao = new MongoPreference(collection)
  def apply(collection: RichCollection) : PreferencesDao = new MongoPreference(collection)

  class MongoPreference(preferencesCollection: RichCollection) extends PreferencesDao {

    override def keysForUser(userId: String): Set[PreferenceKey] = {
      ???
    }

    override def get(key: PreferenceKey): Option[Preferences] = {
      ???
    }

    override def delete(key: PreferenceKey): Option[Preferences] = {
      ???
    }

    override def save(key: PreferenceKey, value: Preferences): Preferences = {
      value
    }
  }

}
