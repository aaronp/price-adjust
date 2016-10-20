package price

import price.fp.Show

import scala.compat.Platform
import scala.io.Source
import scala.util.Try

object ProductPriceFormatter {

  object Args {
    private def lines(fileName: String): Option[List[String]] = {
      Try {
        val src = Source.fromFile(fileName)
        try {
          src.getLines().toList
        } finally {
          src.close
        }
      }.toOption
    }

    object PricePointsFromFile {
      def unapply(fileName: String): Option[PricePoint] = {
        lines(fileName).map { lines =>
          PricePoint(lines.map(_.toInt).toSet)
        }
      }
    }

    object Products {
      def unapply(fileName: String): Option[List[Product[Double]]] = {
        Try {
          val src = Source.fromFile(fileName)
          src.getLines.toList.collect {
            case Product(p) => p
          }
        }.toOption
      }
    }

  }

  def runWithArgs(userArgs: List[String]): Either[Any, String] = {
    userArgs match {
      case List(Args.Products(lines), Args.PricePointsFromFile(points)) =>
        implicit val showOpt: Show[Option[Double]] = Show.optShow[Double]("No price point within min/ / max range")
        implicit object showLine extends Show[(String, Product[Double])] {
          override def show(priceAndProduct : (String, Product[Double])) = {
            ""
          }
        }
        val suggestions = apply(lines, points)
        Right(suggestions.mkString(Platform.EOL))
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
