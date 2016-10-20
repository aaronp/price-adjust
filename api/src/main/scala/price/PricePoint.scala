package price

case class PricePoint(points: Set[Int]) {
  points.foreach { price =>
    require(price >= 0 && price <= 9, s"Invalid price point '$price'. A price point is an integer between 0 and 9")
  }
  def contains(price: Pence) = points.contains(price % 10)
}

object PricePoint {
  def apply(firstPoint: Pence, theRest: Pence*) = {
    new PricePoint(theRest.toSet + firstPoint)
  }
}
