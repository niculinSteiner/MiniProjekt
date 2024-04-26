package domain.person

import domain.PrintTrait
case class PhoneNumber(phoneNumber: String) {
  require(phoneNumber.matches("""^\+?\d{6,18}$"""), "Phonenumber must match the phonenumber-pattern!")
}
