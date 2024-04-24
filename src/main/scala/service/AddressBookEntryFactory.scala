package service

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import domain.address.{Address, City, Street}
import domain.person.{Email, FirstName, LastName, PhoneNumber}
import domain.{AddressBookEntry, Category}

import scala.io.StdIn.readLine

class AddressBookEntryFactory {

  def getAddressBookDataFromUser: IO[AddressBookEntry] = {
    val lastName: IO[LastName] = askAndRead[String]("Was ist Ihr Nachname?").map(name => LastName(name))
    val firstname: IO[FirstName] = askAndRead[String]("Was ist Ihr Vorname?").map(name => FirstName(name))
    val mail: IO[Email] = askAndRead[String]("Was ist Ihre Email?").map(mail => Email(mail))
    val phoneNumber: IO[PhoneNumber] = askAndRead[String]("Was ist Ihr tel. Nummer?").map(name => PhoneNumber(name))
    val address: IO[Address] = askForAddress()
    val category: IO[Category] = askAndRead[Int]("1(BUSINESS), 2(PRIVATE) oder 3(FAMILY)", _.toInt).map(category => Category.fromOrdinal(category - 1))
    IO.delay(AddressBookEntry(get(lastName), get(firstname), get(mail), get(phoneNumber), get(address), get(category)))
  }

  private def askForAddress(): IO[Address] = {
    val street: IO[Street] = askForStreet()
    val city: IO[City] = askAndRead[String]("Wie heisst ihre Stadt/Dorf?").map(city => City(city))
    val zip: IO[Int] = askAndRead[Int]("Was ist Ihre PLZ?", _.toInt).map(zip => zip)
    IO.delay(Address(get(street), get(city), get(zip)))
  }

  private def askForStreet(): IO[Street] = {
    val street: IO[String] = askAndRead[String]("Was ist Ihre Strasse?").map(street => street)
    val number: IO[Int] = askAndRead[Int]("Was ist Ihre Hausnummer?", _.toInt).map(number => number)
    IO.delay(Street(get(number), get(street)))
  }

  private def get[B](ioObject: IO[B]): B = {
    ioObject.unsafeRunSync()
  }

  private def askAndRead[A](question: String, parser: String => A = identity): IO[A] = {
    IO.println(question).unsafeRunSync()
    val input = readLine()
    IO.delay(parser(input))
  }
}
