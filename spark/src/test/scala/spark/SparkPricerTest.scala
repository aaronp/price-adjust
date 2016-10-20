package spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.{Matchers, WordSpec}
import price.{Product, ProductPriceFormatterTest}

class SparkPricerTest extends WordSpec with Matchers {

  "SparkPricer.price" should {
    "return an RDD of products paired w/ their new prices" in {
      val sc = new SparkContext(new SparkConf().setAppName("test"))

      val productsWithPrices: RDD[(Product[Double], Option[Double])] = SparkPricer(sc).price(
        ProductPriceFormatterTest.productPath,
        ProductPriceFormatterTest.pointsPath
      )

      productsWithPrices.collect.foreach(println)
    }
  }

}
