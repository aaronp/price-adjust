package api.service

case class Preferences(preferences : Map[String, String])
case class PreferenceKey(user :String, label : String)
case class PreferencesDto(properties : Map[String, String])