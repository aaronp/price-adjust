package price

object PriceMain extends App {

  def usage = {
    s"""
       |Usage : PriceMain <product file> <price point file>
       |Got ${args.length} args: ${args.mkString(",")}
      """.stripMargin
  }

  val out = ProductPriceFormatter.runWithArgs(args.toList) match {
    case Left(_) => usage
    case Right(output) => output
  }

  println(out)

}
