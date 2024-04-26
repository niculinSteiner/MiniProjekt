package service

import cats.effect.IO
import domain.AddressBookEntry
import domain.address.{Address, City, Street}
import domain.person.{Email, FirstName, LastName, PhoneNumber}
import repository.AppState
import service.IOService.*

object AddressBookEntryFactory {

  def getAddressBookDataFromUser(appState: AppState): IO[AddressBookEntry] = {
    for {
      lastName <- askAndRead("Was ist der Nachname?", input => LastName(input))
      firstName <- askAndRead("Was ist der Vorname?", input => FirstName(input))
      email <- askAndRead("Was ist die Email?", input => parseEmail(appState, Email(input)))
      phoneNumber <- askAndRead("Was ist die Telefonnummer?", input => PhoneNumber(input))
      address <- askForAddress()
      category <- askAndRead("1 für (BUSINESS), 2 für (PRIVATE) oder 3 für (FAMILY)", parseCategory)
    } yield AddressBookEntry(lastName, firstName, email, phoneNumber, address, category)
  }

  private def askForAddress(): IO[Address] = {
    for {
      street <- askForStreet()
      city <- askAndRead("Name von der/dem Stadt/Dorf?", input => City(input))
      zip <- askAndRead("Was ist die PLZ?", _.toInt)
    } yield Address(street, city, zip)
  }

  private def askForStreet(): IO[Street] = {
    for {
      streetName <- askAndRead("Was ist die Straße?")
      number <- askAndRead("Was ist die Hausnummer?", _.toInt)
    } yield Street(number, streetName)
  }

  private def parseEmail(appState: AppState, email: Email): Email = {
    if (emailAlreadyExists(appState, email)) {
      throw new IllegalArgumentException("Entry with this email already exists! Email has to be unique!")
    }
    email
  }

  private def emailAlreadyExists(appState: AppState, email: Email) = {
    appState.addressBookEntryStore.map(_.mail).contains(email)
  }
}
