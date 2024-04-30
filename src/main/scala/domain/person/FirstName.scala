package domain.person

import domain.{FilterTrait, PrintTrait}

case class FirstName(firstName: String)extends FilterTrait, PrintTrait("Vorname: ", firstName)
