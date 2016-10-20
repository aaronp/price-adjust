package price

;

import org.scalatest.{Matchers, WordSpec}

class PriceRangeTest extends WordSpec with Matchers {

  "PriceRange.within" should {
    "return true if a price is within the range, inclusive" in {
      PriceRange(0, 1).within(-1) shouldBe false
      PriceRange(0, 1).within(0) shouldBe true
      PriceRange(0, 1).within(1) shouldBe true
      PriceRange(0, 1).within(2) shouldBe false
    }
  }
}