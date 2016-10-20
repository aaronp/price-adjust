package price.fp

import org.scalatest.{Matchers, WordSpec}

class ShowTest extends WordSpec with Matchers {

  "Show" should {
    "show doubles" in {
      implicitly[Show[Double]].show(1.234) shouldBe "1.23"
      implicitly[Show[Double]].show(1d) shouldBe "1.00"
    }
  }
}