package domain.person

case class PhoneNumber(phoneNumber: String) {
  require(phoneNumber.matches("""^\+?\d{6,18}$"""))
}
