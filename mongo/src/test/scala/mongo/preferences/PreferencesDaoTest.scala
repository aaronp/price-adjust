package mongo.preferences

import api.service.{PreferenceService, PreferenceServiceSpec, Preferences}
import com.github.fakemongo.Fongo
import com.mongodb.{MongoClient, FongoDB}

class PreferencesDaoTest extends PreferenceServiceSpec {
  override def newService: PreferenceService = {

    PreferencesDao("192.168.99.100", 32768, "test")
  }
}
