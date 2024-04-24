package domain

enum Category(category: String) extends PrintTrait("Kategorie: ", category) {
  case BUSINESS extends Category("BUSINESS")
  case PRIVATE extends Category("PRIVATE")
  case FAMILY extends Category("FAMILY")
}
