package domain.person

import domain.PrintTrait

case class LastName(name:String) extends PrintTrait("Nachname: ", lastName)
