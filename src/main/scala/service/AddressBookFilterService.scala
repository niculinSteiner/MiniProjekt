package service

import domain.address.City
import domain.person.{Email, FirstName, LastName, PhoneNumber}
import domain.{AddressBookEntry, Category, FilterTrait}

object AddressBookFilterService {
  def filterBy[T <: FilterTrait](filterArgument: T, entries: List[AddressBookEntry]): List[AddressBookEntry] = {
    filterArgument match {
      case _ : Category => entries.filter(_.category == filterArgument)
      case _ : Email => entries.filter(_.mail == filterArgument)
      case _ : FirstName => entries.filter(_.firstName == filterArgument)
      case _ : LastName => entries.filter(_.lastName == filterArgument)
      case _ : PhoneNumber => entries.filter(_.phoneNumber == filterArgument)
      case _ : City => entries.filter(_.address.city == filterArgument)
      case _  => entries //if nothing found
    }
  }
}
