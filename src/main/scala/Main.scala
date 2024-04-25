import cats.effect.IO
import cats.effect.unsafe.implicits.global
import domain.AddressBookEntry
import repository.AddressBookEntryRepository
import service.AddressBookEntryFactory

var addressBookEntryStore: List[AddressBookEntry] = List.empty

def addEntry(): Unit = {
  addressBookEntryStore = AddressBookEntryRepository().saveNewEntry(addressBookEntryStore, AddressBookEntryFactory().getAddressBookDataFromUser.unsafeRunSync())
  IO.println("Ihre Einträge:\n").unsafeRunSync()
  addressBookEntryStore.foreach(entry => IO.println(s"Adresse ${addressBookEntryStore.indexOf(entry) + 1}: \n$entry").unsafeRunSync())
}

object Main {
  def main(args: Array[String]): Unit = {
    while (true) {
      try {
        addEntry()
      } catch
        case e: IllegalArgumentException => IO.println("Eingabe war falsch!: " + e.getMessage).unsafeRunSync()

    }
  }
}
