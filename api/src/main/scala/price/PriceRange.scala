package price

case class PriceRange(minPence :Pence, maxPrice: Pence) {
  def within(p : Pence) = p >= minPence && p <= maxPrice
}
