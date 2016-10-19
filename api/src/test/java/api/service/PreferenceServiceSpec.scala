package api.service

import org.scalatest.{Matchers, WordSpec, FunSuite}

trait PreferenceServiceSpec extends WordSpec with Matchers {

  def newService : PreferenceService

  val bobKey1 = PreferenceKey("Bob", "some label")
  val bobKey2 = PreferenceKey("Bob", "another label")
  val sueKey1 = PreferenceKey("Sue", "some label")
  val sueKey2 = PreferenceKey("Sue", "meh")

  val prefs1 = Preferences(Map("name" -> "one"))
  val prefs2 = Preferences(Map("foo" -> "bar", "fizz" -> "buzz"))

  "PreferenceService.keysForUser" should {
    "return the keys for a user" in {
      val service = newService
      service.keysForUser("Bob") should be (empty)
      service.keysForUser("Sue") should be (empty)
      service.save(bobKey1, prefs1)
      service.keysForUser("Bob") should contain only (bobKey1)
      service.keysForUser("Sue") should be (empty)

      service.delete(bobKey1).toList should contain only(prefs1)
      service.delete(bobKey1).toList should be(empty)
      service.keysForUser("Bob") should be (empty)
      service.keysForUser("Sue") should be (empty)
    }
  }

  "PreferenceService.get" should {
    "return the keys for a key" in {
      val service = newService
      service.get(sueKey1) should be (empty)
      service.save(sueKey1, prefs1)
      service.get(sueKey1).toList should contain only(prefs1)
      service.save(sueKey1, prefs2)
      service.get(sueKey1).toList should contain only(prefs2)
    }
  }
}
