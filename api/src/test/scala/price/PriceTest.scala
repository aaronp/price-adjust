package price

import org.scalatest.{Matchers, WordSpec}

import scala.math._

class PriceTest extends WordSpec with Matchers {

  "Price.fromPence" should {
    "convert pence to a big decimal" in {
      Price.fromPence[BigDecimal](123) shouldBe BigDecimal("1.23")
    }
    "convert pence to a double" in {
      Price.fromPence[Double](123) shouldBe 1.23
    }
  }
  "Price.asPence" should {
    "convert a big decimal to pence" in {
      Price.asPence(BigDecimal("1.23456")) shouldBe 123
    }
    "convert a double to pence" in {
      Price.asPence(1.0001) shouldBe 100
    }
  }
}