# Adressbuch

## Was soll die App können?

- Adressbucheintrag (Name, Nachname, Adresse, Kategorie, Tel(Unique), Mail) hinzufügen
- Filterfunktion 
- Sortierfunktion
- Suchfunktion
- Alles Anzeigen

## Doku
### Entitäten
- Address(street: String, city: String, zip: Int)
- Category(case BUSINESS, PRIVATE, FAMILY)
- AddressBookEntry(lastName:String, firstName:String, phoneNumber:String, mail: String, category: Category, address:Address)
