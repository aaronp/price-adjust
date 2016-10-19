package mongo.preferences

import api.service.{PreferenceService, PreferenceServiceSpec, Preferences}
import com.github.fakemongo.Fongo
import com.mongodb.FongoDB

class PreferencesDaoTest extends PreferenceServiceSpec {
  override def newService: PreferenceService = {
    val fongo: Fongo = new Fongo(getClass.getSimpleName)
    val db: FongoDB = fongo.getDB(getClass.getSimpleName)

    import mongo.Implicits._

    val prefsCollection : RichCollection  = db.getCollection(collectionName[Preferences])
    PreferencesDao(prefsCollection)
  }
}
