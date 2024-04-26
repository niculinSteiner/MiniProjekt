package domain.person

import domain.PrintTrait

case class FirstName(firstName: String) extends PrintTrait("Vorname: ", firstName)
