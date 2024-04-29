package domain.address

import domain.{FilterTrait, PrintTrait}
case class City(name:String)extends FilterTrait extends PrintTrait("Stadt: ", name){}
