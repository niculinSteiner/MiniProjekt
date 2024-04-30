package domain

trait PrintTrait(prefix: String, value: String):
  override def toString: String = "\n" + prefix + value
