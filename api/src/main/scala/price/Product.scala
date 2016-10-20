package price

import scala.util.Try

case class Product[T: Fractional](name: String, originalPrice: T, minPrice: T, maxPrice: T) {
  def minMaxRange = {
    import Price.Implicits._
    PriceRange(minPrice, maxPrice)
  }
}

object Product {

  private object LinePrice {
    def unapply(price: String): Option[Double] = {
      Try(price.toDouble).toOption
    }
  }

  def unapply(line: String): Option[Product[Double]]  = fromCsv(line)

  def fromCsv(line: String): Option[Product[Double]] = {
    line.split(",", -1).toList.map(_.trim) match {
      case List(name, LinePrice(price), LinePrice(min), LinePrice(max)) =>
        Option(Product[Double](name, price, min, max))
      case _ => None
    }
  }
}
