package domain.person

import domain.{FilterTrait, PrintTrait}

case class LastName(name:String) extends FilterTrait, PrintTrait("Nachname: ", name)
