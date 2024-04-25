import cats.effect.unsafe.implicits.global
import domain.person.Email
import domain.{AddressBookEntry, Category}
import repository.AddressBookEntryRepository
import repository.AddressBookEntryRepository.*
import service.{AddressBookEntryFactory, AddressBookFilterService}
import service.AddressBookEntryFactory.*
import service.AddressBookFilterService.*
import service.IOService.*

var addressBookEntryStore: List[AddressBookEntry] = List.empty
val actionFinishedPlaceHolder: String = "\nAction finished!" + "" +
  "\n----------------------------------------------------------\n\n"

object Main {
  def main(args: Array[String]): Unit = {
    applicationLoop(false)
  }

  private def applicationLoop(showActionFinished: Boolean = true): Unit = {
    if (showActionFinished) println(actionFinishedPlaceHolder)
    val choice: Unit = askAndRead("Was möchten Sie tun?\n" +
      "1. Eintrag hinzufügen \n" +
      "2. Filtern\n" +
      "4. Beenden").unsafeRunSync()
    match {
      case "1" =>
        try {
          addEntry()
        } catch
          case e: IllegalArgumentException => println("Eingabe war falsch!: " + e.getMessage); addEntry()
          case unexpected: Exception => println("Unerwarteter Fehler aufgetreten" + unexpected.getCause)

      case "2" => showEntriesByFilter()
      case "4" =>
        println("Auf Wiedersehen!")
        System.exit(0)
      case _ => println("Ungültige Eingabe!"); applicationLoop(false)
    }
  }

  private def addEntry(): Unit = {
    addressBookEntryStore = saveNewEntry(addressBookEntryStore, getAddressBookDataFromUser.unsafeRunSync())
    printStore(addressBookEntryStore)
    applicationLoop()
  }

  private def printStore(entries: List[AddressBookEntry]): Unit = {
    println("Ihre Einträge:\n")
    entries.foreach(entry => println(s"Adresse ${entries.indexOf(entry) + 1}: \n$entry"))
  }

  private def showEntriesByFilter(): Unit = {
    val filterType: Unit = askAndRead("Nach welchem Attribute soll gefilterd werden?\n" +
      "1. Kategorie\n" +
      "2. Email").unsafeRunSync()
    match {
      case "1" =>
        val category = for {
          input <- askAndRead("Wie heisst die Kategorie?")
        } yield Category.valueOf(input)
        printStore(filterBy(category.unsafeRunSync(), addressBookEntryStore))
        applicationLoop()
      case "2" =>
        val email = for {
          input <- askAndRead("Wie heisst die Email?")
        } yield Email(input)
        printStore(filterBy(email.unsafeRunSync(), addressBookEntryStore))
        applicationLoop()
      case _ => println("Ungültige Eingabe!"); applicationLoop()
    }
  }
}
