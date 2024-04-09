package domain.address

case class Address(street: Street, city: City, zip: Int){
  require(zip.toString.length == 4)
}
