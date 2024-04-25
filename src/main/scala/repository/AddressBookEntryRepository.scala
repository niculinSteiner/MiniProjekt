package repository

import domain.AddressBookEntry

object AddressBookEntryRepository {
    def saveNewEntry(store: List[AddressBookEntry], entryToSave: AddressBookEntry): List[AddressBookEntry] =  {
    entryToSave :: List.from[AddressBookEntry](store) 
  }
}
