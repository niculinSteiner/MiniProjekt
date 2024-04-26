package repository

import domain.AddressBookEntry

case class AppState(addressBookEntryStore: List[AddressBookEntry] = List.empty)
