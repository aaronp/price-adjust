package price

import org.scalatest.{Matchers, WordSpec}

class ProductTest extends WordSpec with Matchers {

  "Product.ffromCsv" should {
    "parse A, 2.91, 2.89, 2.97" in {
      Product.fromCsv(" A, 2.91, 2.89, 2.97") shouldBe Option(Product("A", 2.91, 2.89, 2.97))
    }
  }
}
