package spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.{Matchers, WordSpec}
import price.{Product, ProductPriceFormatter, ProductPriceFormatterTest}

class SparkPricerTest extends WordSpec with Matchers {

  "SparkPricer.price" should {
    "return an RDD of products paired w/ their new prices" in {
      val sc = new SparkContext(new SparkConf().setMaster("local").setAppName("test"))

      import ProductPriceFormatter._
      val productRdd = {
        val Args.Products(products) = ProductPriceFormatterTest.productPath
        sc.parallelize(products)
      }

      val Args.PricePointsFromFile(points) = ProductPriceFormatterTest.pointsPath

      val productsWithPricesRDD: RDD[(Product[Double], Option[Double])] = {
        SparkPricer(sc).priceProducts(points, productRdd)
      }

      val productsWithPrices = productsWithPricesRDD.collect

      productsWithPrices.length shouldBe 3
      productsWithPrices should contain only(
        (Product("A", 2.91, 2.89, 2.97), Some(2.92)),
        (Product("B", 3.64, 3.69, 3.73), Some(3.72)),
        (Product("C", 3.65, 3.65, 3.66), None)
        )
    }
  }
}
