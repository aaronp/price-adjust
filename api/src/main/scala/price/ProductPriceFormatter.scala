package price

import price.fp.Show

import scala.compat.Platform
import scala.io.Source
import scala.util.Try

object ProductPriceFormatter {

  private object Args {
    private def lines(fileName: String): Option[List[String]] = {
      try {
        val src = Source.fromFile(fileName)
        val lines = try {
          src.getLines().toList
        } finally {
          src.close
        }
        Option(lines)
      } catch {
        case _ => None
      }
    }

    private def asInt(s: String) = Try(s.toInt).toOption

    object PricePointsFromFile {
      def unapply(fileName: String): Option[PricePoint] = {
        val points: Option[Set[Pence]] = lines(fileName).map(_.flatMap(asInt).toSet)
        points.map(PricePoint.apply)
      }
    }

    object Products {
      def unapply(fileName: String): Option[List[Product[Double]]] = {
        lines(fileName).map { all =>
          all.collect { case Product(p) => p }
        }
      }
    }

  }

  def runWithArgs(userArgs: List[String]): Either[Any, String] = {
    userArgs match {
      case List(Args.Products(lines), Args.PricePointsFromFile(points)) =>
        implicit val showOpt: Show[Option[Double]] = Show.optShow[Double]("No price point within min/ / max range")
        implicit object showLine extends Show[(String, Product[Double])] {
          override def show(priceAndProduct: (String, Product[Double])) = {
            val (price, product) = priceAndProduct
            import product._
            s"$name, $originalPrice, $minPrice, $maxPrice, $price"
          }
        }
        val suggestions = apply(lines, points)
        val header = "Product, Original Price, Min Price, Max Price, New Price"
        Right((header +: suggestions).mkString(Platform.EOL))
      case other => Left(other)
    }

  }

  def apply[T](products: List[Product[T]], points: PricePoint)(implicit f: Fractional[T], showSuggestion: Show[Option[T]], showLine: Show[(String, Product[T])]) = {
    //    val show = implicitly[Show[T]]
    products.map { product =>
      val suggestion: Option[T] = PriceSuggestion(product.minMaxRange, points).suggestPrice(product.originalPrice)
      val suggString = showSuggestion.show(suggestion)
      val line: String = showLine.show(suggString -> product)
      line
    }

  }


}
