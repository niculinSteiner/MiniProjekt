package service

import cats.effect.IO
import domain.address.{Address, City, Street}
import domain.person.{Email, FirstName, LastName, PhoneNumber}
import domain.{AddressBookEntry, Category}

class AddressBookEntryFactory {

  def getAddressBookDataFromUser: IO[AddressBookEntry] = {
    for {
      lastName <- askAndRead("Was ist Ihr Nachname?", input => LastName(input))
      firstName <- askAndRead("Was ist Ihr Vorname?", input => FirstName(input))
      email <- askAndRead("Was ist Ihre Email?", input => Email(input))
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

  private def askAndRead[A](question: String, parser: String => A = identity): IO[A] = {
    for {
      _ <- IO.println(question)
      input <- IO.readLine
    } yield parser(input)
  }

  private def parseCategory(input: String): Category = {
    val categoryIndex = input.toInt - 1
    Category.fromOrdinal(categoryIndex)
  }
}
