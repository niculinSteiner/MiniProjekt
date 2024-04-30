package domain.address

import domain.PrintTrait
case class Street(houseNumber: Int, name: String) extends PrintTrait("Strasse: ", name + " " + houseNumber) {
  require(houseNumber >= 0, "House number must be non-negative")
}
