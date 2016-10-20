package price

import java.net.URL
import java.nio.file.Paths

import org.scalatest.{Matchers, WordSpec}

class ProductPriceFormatterTest extends WordSpec with Matchers {

  import ProductPriceFormatterTest._
  "ProductPriceFormatter.runWithArgs" should {
    "produce a report" in {

      val Right(output) = ProductPriceFormatter.runWithArgs(List(productPath, pointsPath))

      val expectedOutput = """Product, Original Price, Min Price, Max Price, New Price
                             |A, 2.91, 2.89, 2.97, 2.92
                             |B, 3.64, 3.69, 3.73, 3.72
                             |C, 3.65, 3.65, 3.66, No price point within min / max range""".stripMargin

      output shouldBe expectedOutput
    }
  }
}


object ProductPriceFormatterTest {

  val List(productPath, pointsPath) = List("products.csv", "points.txt").map { fileName =>
    val res: URL = getClass.getClassLoader.getResource(fileName)
    val path = Paths.get(res.toURI)
    path.toString
  }
}