package mongo.preferences

import api.service._
import api.service.json.JsonSupport
import io.circe.generic.JsonCodec

@JsonCodec
case class SavedPreferences(id: String, created: Long, key: PreferenceKey, preferences: Preferences)

object SavedPreferences extends JsonSupport {

  import io.circe._
  import io.circe.generic.semiauto._

//  implicit val savedPreferencesDecoder: Decoder[SavedPreferences] = deriveDecoder
//  implicit val savedPreferencesEncoder: Encoder[SavedPreferences] = deriveEncoder
}
