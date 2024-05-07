import cats.effect.unsafe.implicits.global
import domain.address.{Address, City, Street}
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
val demoData: List[AddressBookEntry] = List(
  AddressBookEntry(LastName("steiner"), FirstName("niculin"), Email("m@m.ch"), PhoneNumber("0777777777"), Address(Street(9, "halden"), City("benken"), 8778), Category.BUSINESS),
  AddressBookEntry(LastName("a"), FirstName("b"), Email("m@m.ch"), PhoneNumber("0777777777"), Address(Street(9, "halden"), City("benken"), 8778), Category.BUSINESS))

object Main {
  def main(args: Array[String]): Unit = {
    applicationLoop(false, AppState(demoData))
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
      "4. Sortieren\n" +
      "5. Beenden").unsafeRunSync()
    match {
      case "1" =>
        try {
          val newState = addEntry(appState)
          printStore(newState.addressBookEntryStore)
          newState
        } catch
          case e: IllegalArgumentException => println("Eingabe war falsch!: " + e.getMessage); AppState(appState.addressBookEntryStore);
          case unexpected: Exception => println("Unerwarteter Fehler aufgetreten" + unexpected.getCause); AppState(appState.addressBookEntryStore);
      case "2" =>
        try {
          printStore(showEntriesByFilter(appState).addressBookEntryStore)
          appState
        } catch
          case e: IllegalArgumentException => println("Eingabe war falsch!: " + e.getMessage); AppState(appState.addressBookEntryStore);
          case unexpected: Exception => println("Unerwarteter Fehler aufgetreten" + unexpected.getCause); AppState(appState.addressBookEntryStore);
      case "3" =>
        printStore(appState.addressBookEntryStore)
        appState;
      case "4" =>
        try {
          printStore(sortEntries(appState).addressBookEntryStore)
          appState
        } catch
          case e: IllegalArgumentException => println("Eingabe war falsch!: " + e.getMessage); AppState(appState.addressBookEntryStore);
          case unexpected: Exception => println("Unerwarteter Fehler aufgetreten" + unexpected.getCause); AppState(appState.addressBookEntryStore);
      case "5" =>
        println("Auf Wiedersehen!")
        System.exit(0)
      case _ => println("Diese Aktion gibts nicht!"); appState;
    }
    applicationLoop(true, newAppState.asInstanceOf[AppState])
  }

  private def addEntry(appState: AppState): AppState = {
    saveNewEntry(appState, getAddressBookDataFromUser(appState).unsafeRunSync())
  }

  private def sortEntries(appState: AppState): AppState = {
    val sortedEntries: AppState = askAndRead("Nach welchem Attribute soll sortiert werden?\n" +
      "1. Kategorie\n" +
      "2. Email\n" +
      "3. Vorname\n" +
      "4. Nachname\n" +
      "5. Telefonnummer\n" +
      "6. Stadt\n" +
      "7. Zurück").unsafeRunSync()
    match {
      case "1" => AppState(appState.addressBookEntryStore.sortBy(_.category.ordinal))
      case "2" => AppState(appState.addressBookEntryStore.sortBy(_.mail.email.toLowerCase))
      case "3" => AppState(appState.addressBookEntryStore.sortBy(_.firstName.firstName.toLowerCase))
      case "4" => AppState(appState.addressBookEntryStore.sortBy(_.lastName.name.toLowerCase))
      case "5" => AppState(appState.addressBookEntryStore.sortBy(_.phoneNumber.phoneNumber.toLowerCase))
      case "6" => AppState(appState.addressBookEntryStore.sortBy(_.address.city.name.toLowerCase))
      case "7" => appState
      case _ => println("Ungültige Eingabe!"); showEntriesByFilter(appState)
    }
    sortedEntries
  }

  private def printStore(entries: List[AddressBookEntry]): Unit = {
    println("Ihre Einträge:")
    entries.foreach(entry => println(s"\nAdresse ${entries.indexOf(entry) + 1}: \n$entry"))
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
          input <- askAndRead("Wie lautet die Kategorie? \n1. BUSINESS\n2. PRIVATE\n3. FAMILY", parseCategory)
        } yield input
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
