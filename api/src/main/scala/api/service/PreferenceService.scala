package api.service

trait PreferenceService extends CrudService[PreferenceKey, Preferences] {
  def keysForUser(userId : String) : Set[PreferenceKey]
}