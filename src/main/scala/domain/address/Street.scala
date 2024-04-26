package domain.address

import domain.PrintTrait
case class Street(houseNumber: Int, name: String) {
  require(houseNumber >= 0, "House number must be non-negative")
}
