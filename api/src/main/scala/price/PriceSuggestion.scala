package price

/**
  * Suggests a new price which takes as an input an existing price and provides a new price
  */
trait PriceSuggestion {
  def suggestPrice[T: Fractional](priceInPounds: T): Option[T]
}

object PriceSuggestion {

  private val ValidChanges = {
    (1 to 9).toStream.flatMap { i =>
      Set(i, i * -1)
    }
  }

  class Suggestion(range: PriceRange, points: PricePoint) extends PriceSuggestion {

    override def suggestPrice[T: Fractional](priceInPounds: T): Option[T] = {
      val pence = Price.asPence(priceInPounds)
      val prices: Stream[Pence] = for {
        incOrDec <- ValidChanges
        newPrice = pence + incOrDec
        _ = println(s"$pence + $incOrDec = $newPrice .... valid range? ${range.within(newPrice)} valid price? ${points.contains(newPrice)}")
        if range.within(newPrice)
        if points.contains(newPrice)
      } yield newPrice

      println(prices.mkString(","))
      println()
      val firstInPence: Option[Pence] = prices.headOption
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