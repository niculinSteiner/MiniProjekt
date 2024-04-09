package domain

import domain.address.Address
import domain.person.{Email, FirstName, LastName, PhoneNumber}

case class AddressBookEntry(lastName: LastName, firstName: FirstName, mail: Email, phoneNumber: PhoneNumber, address: Address, category: Category)
