package price

case class PriceRange(minPence: Pence, maxPence: Pence) {
  require(minPence <= maxPence, s"Invalid range $minPence and $maxPence")

  def within(p: Pence) = p >= minPence && p <= maxPence
}
