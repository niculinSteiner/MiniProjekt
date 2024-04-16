package service

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import domain.{AddressBookEntry, Category}
import domain.address.{Address, City, Street}
import domain.person.{Email, FirstName, LastName, PhoneNumber}

import scala.io.StdIn.readLine

class IOService {

  def askAndRead[A](question: String): IO[A] = {
    IO.println(question).unsafeRunSync()
    val input = readLine()
    IO.delay(input.asInstanceOf[A])
  }

  def getAddressBookDataFromUser(): IO[AddressBookEntry] = {
    val lastName: IO[LastName] = askAndRead[String]("Was ist Ihr Name?").map(name => LastName(name))
    val firstname: IO[FirstName] = askAndRead[String]("Was ist Ihr Name?").map(name => FirstName(name))
    val mail: IO[Email] = askAndRead[String]("Was ist Ihr Name?").map(mail => Email(mail))
    val phoneNumber: IO[PhoneNumber] = askAndRead[String]("Was ist Ihr Name?").map(name => PhoneNumber(name))
    val address: IO[Address] = askForAddress()
    val category: IO[Category] = askAndRead[Category]("test").map(category => category)
    
  }

  def askForAddress(): IO[Address] = {
    val street: IO[Street] = askForStreet()
    val city: IO[City] = askAndRead[String]("Was ist Ihr Name?").map(city => City(city))
    val zip: IO[Int] = askAndRead("Was ist Ihre Hausnummer?").map(zip => zip)
    IO.delay(Address(street.unsafeRunSync(), city.unsafeRunSync(), zip.unsafeRunSync()))
  }

  def askForStreet(): IO[Street] = {
    val street: IO[String] = askAndRead[String]("Was ist Ihre Strasse?").map(street => street)
    val number: IO[Int] = askAndRead("Was ist Ihre Hausnummer?").map(number => number)
    IO.delay(Street(number.unsafeRunSync(), street.unsafeRunSync()))
  }


}
