package spark

import org.apache.spark.SparkContext
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import price.{PricePoint, PriceSuggestion, Product}

import scala.util.Try

case class SparkPricer(sc: SparkContext) {

  def asProducts(csvLines: RDD[String]): RDD[Product[Double]] = {
    csvLines.collect {
      case Product(p) => p
    }
  }

  def asPricePoints(csvLines: RDD[String]): PricePoint = {
    val points: RDD[Int] = csvLines.map(_.trim).collect {
      case line if Try(line.toInt).isSuccess => line.toInt
    }
    val total = points.distinct.count()
    require(total <= 10, s"Expected a small price point file, but there seem to be $total price points!")
    PricePoint(points.collect().toSet)
  }

  def priceProducts(points: PricePoint, products: RDD[Product[Double]]): RDD[(Product[Double], Option[Double])] = {
    val b: Broadcast[PricePoint] = sc.broadcast(points)
    products.map { product =>
      val newPrice = PriceSuggestion(product.minMaxRange, b.value).suggestPrice(product.originalPrice)
      product -> newPrice
    }
  }

  def price(priceFileLocation: String, pointsLocation: String) = {
    val points: PricePoint = asPricePoints(sc.textFile(priceFileLocation))
    val products: RDD[Product[Double]] = asProducts(sc.textFile(pointsLocation))
    priceProducts(points, products)
  }
}
