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
      lastName <- askAndRead("Was ist Ihr Nachname?", input => LastName(input))
      firstName <- askAndRead("Was ist Ihr Vorname?", input => FirstName(input))
      email <- askAndRead("Was ist Ihre Email?", input => parseEmail(appState, Email(input)))
      phoneNumber <- askAndRead("Was ist Ihre Telefonnummer?", input => PhoneNumber(input))
      address <- askForAddress()
      category <- askAndRead("1(BUSINESS), 2(PRIVATE) oder 3(FAMILY)", parseCategory)
    } yield AddressBookEntry(lastName, firstName, email, phoneNumber, address, category)
  }

  private def askForAddress(): IO[Address] = {
    for {
      street <- askForStreet()
      city <- askAndRead("Wie heißt ihre Stadt/Dorf?", input => City(input))
      zip <- askAndRead("Was ist Ihre PLZ?", _.toInt)
    } yield Address(street, city, zip)
  }

  private def askForStreet(): IO[Street] = {
    for {
      streetName <- askAndRead("Was ist Ihre Straße?")
      number <- askAndRead("Was ist Ihre Hausnummer?", _.toInt)
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
