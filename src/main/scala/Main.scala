import domain.AddressBookEntry
import domain.Category.FAMILY
import domain.address.{Address, City, Street}
import domain.person.{Email, FirstName, LastName, PhoneNumber}

object Main {
  def main(args: Array[String]): Unit = {
    println(AddressBookEntry(LastName("Steiner"), FirstName("Niculin"), Email("steiner.niculin@mail.ch"), PhoneNumber("0775270771"), Address(Street(9, "Strasse"), City("Benken"), 8717), FAMILY))
  }
}
