package price

import org.scalatest.{Matchers, WordSpec}


class PricePointTest extends WordSpec with Matchers {

  "PricePoint.contains" should {
    val validPoints = Set("1", "5", "9")
    val points = PricePoint(1, 5, 9)
    (0 to 20).foreach {
      case n =>
        val expected = validPoints.contains(n.toString.last.toString)
        s"return $expected for $n" in {
          points.contains(n) shouldBe expected
        }
    }
    "return true for 1234567891" in {
      points.contains(1234567891)
    }
  }
}
