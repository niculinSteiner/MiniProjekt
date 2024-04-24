package repository

import domain.AddressBookEntry

class AddressBookEntryRepository {


  def saveNewEntry(store: List[AddressBookEntry], entryToSave: AddressBookEntry): List[AddressBookEntry] =  {
    entryToSave :: List.from[AddressBookEntry](store) 
  }
}
