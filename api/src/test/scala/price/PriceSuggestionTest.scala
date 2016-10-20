package price

import org.scalatest.{Matchers, WordSpec}

class PriceSuggestionTest extends WordSpec with Matchers {

  "PriceSuggestion.suggestPrice" should {
    import Price.Implicits._
    val points = PricePoint(2, 3, 7)
    val range = PriceRange(2.89, 2.97)
    val suggester = PriceSuggestion(range, points)

    val testCases = Seq(
      (2.86, Set(2.89)),
      (2.87, Set(2.89)),
      (2.88, Set(2.89)),
      (2.89, Set(2.92)),
      (2.90, Set(2.89)),
      (2.91, Set(2.92)),
      (2.92, Set(2.93)),
      (2.94, Set(2.93)),
      (2.95, Set(2.93, 2.97)),
      (2.96, Set(2.97)),
      (2.97, Set(2.93)),
      (2.98, Set(2.97)),
      (2.99, Set(2.97)),
      (3.00, Set(2.97))
    )

    testCases.map {
      case (inputPrice, expected) => (inputPrice, expected.toList)
    }.foreach {
      case (inputPrice, List(expected)) =>
        s"convert $inputPrice to $expected" in {
          suggester.suggestPrice(inputPrice).toList.map(_.asPence) should contain only (expected.asPence)
        }
      case (inputPrice, List(a, b)) =>
        s"convert $inputPrice to either $a or $b" in {
          suggester.suggestPrice(inputPrice).toList.map(_.asPence) should contain oneOf(a.asPence, b.asPence)
        }
    }
  }
}