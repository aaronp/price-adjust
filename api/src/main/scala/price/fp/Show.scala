package price.fp

trait Show[T] {
  def show(value: T): String
}

object Show {
  implicit object DoubleShow extends Show[Double] {
    override def show(dbl: Double) = {
      "%.2f".format(dbl)
    }
  }
  def optShow[T: Show](onNone: String): Show[Option[T]] = {
    new Show[Option[T]] {
      override def show(opt: Option[T]) = {
        opt match {
          case Some(t) => implicitly[Show[T]].show(t)
          case None => onNone
        }
      }
    }
  }
}