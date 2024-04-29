package domain.person

import domain.{FilterTrait, PrintTrait}
case class PhoneNumber(phoneNumber: String)extends FilterTrait extends PrintTrait("Telefonnummer: ", phoneNumber){
  require(phoneNumber.matches("""^\+?\d{6,18}$"""), "Phonenumber must match the phonenumber-pattern!")
}
