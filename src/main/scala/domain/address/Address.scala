package domain.address

import domain.PrintTrait

case class Address(street: Street, city: City, zip: Int) extends PrintTrait("Addresse: ", "\n\tZip: " +zip.toString + city + street) {
  require(zip.toString.length == 4, "Zip Code length must be 4!")
}
