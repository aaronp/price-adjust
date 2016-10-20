package price

import org.scalatest.{Matchers, WordSpec}

class ProductPriceFormatterTest extends WordSpec with Matchers {

  "ProductPriceFormatter.run" should {
    "work" in {
      val args = List("products.csv", "points.csv")
      val Right(out) = ProductPriceFormatter.runWithArgs(args)

      println(out)
    }
  }
}
