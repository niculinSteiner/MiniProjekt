package domain.person

import domain.FilterTrait

case class Email(email: String) extends FilterTrait{
  require(email.matches("""^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$"""), "Must match the mail pattern!")
}
