package domain.person

case class Email(email: String){
  require(email.matches("""^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$"""), "Must match the mail pattern!")
}
