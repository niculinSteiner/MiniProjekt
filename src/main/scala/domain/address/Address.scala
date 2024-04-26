package domain.address

import domain.PrintTrait
case class Address(street: Street, city: City, zip: Int){
  require(zip.toString.length == 4, "Zip Code length must be 4!")
}
