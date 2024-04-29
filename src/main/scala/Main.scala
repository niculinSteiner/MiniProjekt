import cats.effect.unsafe.implicits.global
import domain.address.City
import domain.person.{Email, FirstName, LastName, PhoneNumber}
import domain.{AddressBookEntry, Category}
import repository.AddressBookEntryRepository.*
import repository.{AddressBookEntryRepository, AppState}
import service.AddressBookEntryFactory.*
import service.AddressBookFilterService.*
import service.IOService.*
import service.{AddressBookEntryFactory, AddressBookFilterService}

import scala.annotation.tailrec

val actionFinishedPlaceHolder: String = "\nAction finished!" + "" +
  "\n----------------------------------------------------------\n\n"

object Main {
  def main(args: Array[String]): Unit = {
    applicationLoop(false, AppState())
  }

  @tailrec
  private def applicationLoop(showActionFinished: Boolean = true, appState: AppState): Unit = {
    if (showActionFinished) {
      println(actionFinishedPlaceHolder)
    }
    val newAppState = askAndRead("Was möchten Sie tun?\n" +
      "1. Eintrag hinzufügen \n" +
      "2. Filtern\n" +
      "3. Alle Eintäge anzeigen\n" +
      "4. Beenden").unsafeRunSync()
    match {
      case "1" =>
        try {
          val newState = addEntry(appState)
            printStore(newState.addressBookEntryStore)
            newState
        } catch
          case e: IllegalArgumentException => println("Eingabe war falsch!: " + e.getMessage); AppState(appState.addressBookEntryStore);
          case unexpected: Exception => println("Unerwarteter Fehler aufgetreten" + unexpected.getCause); AppState(appState.addressBookEntryStore);
      case "2" => printStore(showEntriesByFilter(appState).addressBookEntryStore); appState;
      case "3" => printStore(appState.addressBookEntryStore); appState;
      case "4" =>
        println("Auf Wiedersehen!")
        System.exit(0)
      case _ => println("Ungültige Eingabe!");
    }
    applicationLoop(true, newAppState.asInstanceOf[AppState])
  }
  
  private def addEntry(appState: AppState): AppState = {
    saveNewEntry(appState, getAddressBookDataFromUser(appState).unsafeRunSync())
  }

  private def printStore(entries: List[AddressBookEntry]): Unit = {
    println("Ihre Einträge:\n")
    entries.foreach(entry => println(s"Adresse ${entries.indexOf(entry) + 1}: \n$entry"))
  }

  private def showEntriesByFilter(appState: AppState): AppState = {
    val filteredEntries: AppState = askAndRead("Nach welchem Attribute soll gefilterd werden?\n" +
      "1. Kategorie\n" +
      "2. Email\n" +
      "3. Vorname\n" +
      "4. Nachname\n" +
      "5. Telefonnummer\n" +
      "6. Stadt\n" +
      "7. Zurück").unsafeRunSync()
    match {
      case "1" =>
        val category = for {
          input <- askAndRead("Wie lautet die Kategorie?")
        } yield Category.valueOf(input)
        AppState(filterBy(category.unsafeRunSync(), appState.addressBookEntryStore))
      case "2" =>
        val email = for {
          input <- askAndRead("Wie lautet die Email?")
        } yield Email(input)
        AppState(filterBy(email.unsafeRunSync(), appState.addressBookEntryStore))
      case "3" =>
        val firstName = for {
          input <- askAndRead("Wie lautet der Vorname?")
        } yield FirstName(input)
        AppState(filterBy(firstName.unsafeRunSync(), appState.addressBookEntryStore))
      case "4" =>
        val lastName = for {
          input <- askAndRead("Wie lautet der Nachname?")
        } yield LastName(input)
        AppState(filterBy(lastName.unsafeRunSync(), appState.addressBookEntryStore))
      case "5" =>
        val phoneNumber = for {
          input <- askAndRead("Wie lautet die Telefonnummer?")
        } yield PhoneNumber(input)
        AppState(filterBy(phoneNumber.unsafeRunSync(), appState.addressBookEntryStore))
      case "6" =>
        val city = for {
          input <- askAndRead("Wie lautet die Stadt?")
        } yield City(input)
        AppState(filterBy(city.unsafeRunSync(), appState.addressBookEntryStore))
      case "7" => appState
      case _ => println("Ungültige Eingabe!"); showEntriesByFilter(appState)
    }
    filteredEntries
  }
}
