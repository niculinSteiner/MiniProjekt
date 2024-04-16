import cats.effect.IO
import cats.effect.unsafe.implicits.global
import domain.AddressBookEntry
import domain.person.{FirstName, LastName}
import service.IOService

object Main {
  val addressBookEntryStore: List[AddressBookEntry] = List.empty

  def main(args: Array[String]): Unit = {
    getAddressBookDataFromUser()
  }


}
