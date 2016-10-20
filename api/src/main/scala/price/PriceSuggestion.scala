package price

/**
  * Suggests a new price which takes as an input an existing price and provides a new price
  */
trait PriceSuggestion {
  def suggestPrice[T: Fractional](priceInPounds: T): Option[T]
}

object PriceSuggestion {

  class Suggestion(range: PriceRange, points: PricePoint) extends PriceSuggestion {
    private val validChanges = {
      val ints = (1 to 9).filter(points.contains)
      ints.flatMap { i =>
        Set(i, i * -1)
      }
    }

    override def suggestPrice[T: Fractional](priceInPounds: T): Option[T] = {
      val pence = Price.asPence(priceInPounds)
      val prices: Iterator[Pence] = for {
        incOrDec <- validChanges.iterator
        newPrice = pence + incOrDec
        if range.within(newPrice)
      } yield newPrice

      val firstInPence: Option[Pence] = prices.toStream.headOption
      firstInPence.map { pence =>
        Price.fromPence[T](pence)
      }
    }
  }

  /**
    * @param range  the price range which the new price must be within
    * @param points the price point which the new price must end in
    * @return a price suggestion which prives a new price within a range and price point
    */
  def apply(range: PriceRange, points: PricePoint): PriceSuggestion = {
    new Suggestion(range, points)
  }
}