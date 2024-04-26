package repository

import domain.AddressBookEntry

object AddressBookEntryRepository {
  def saveNewEntry(appState: AppState, entryToSave: AddressBookEntry): AppState = {
    AppState(entryToSave :: appState.copy().addressBookEntryStore)
  }
}
