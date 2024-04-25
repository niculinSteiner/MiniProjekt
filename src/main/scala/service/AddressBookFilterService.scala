package service

import domain.person.Email
import domain.{AddressBookEntry, Category, FilterTrait}

object AddressBookFilterService {
  def filterBy[T <: FilterTrait](filterArgument: T, entries: List[AddressBookEntry]): List[AddressBookEntry] = {
    filterArgument match {
      case _ : Category => entries.filter(_.category == filterArgument)
      case _ : Email => entries.filter(_.mail == filterArgument)
      case _  => entries //if nothing found
    }
  }
}
