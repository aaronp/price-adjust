package price

object Price {


  object Implicits {
    implicit def numericToPence[T: Numeric](p: T): Pence = asPence(p)

    implicit class RichNumeric[T](val p: T) extends AnyVal {
      def asPence(implicit ev: Numeric[T]) = Price.asPence(p)
    }

  }
  def fromPence[T: Fractional](price: Pence): T = {
    val num = implicitly[Fractional[T]]
    val oneHundred = num.fromInt(100)
    val asT = num.fromInt(price)
    num.div(asT, oneHundred)
  }
  def asPence[T: Numeric](price: T): Pence = {
    val num = implicitly[Numeric[T]]
    val pence = num.times(price, num.fromInt(100))
    num.toInt(pence)
  }

}
