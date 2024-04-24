package domain

trait PrintTrait(prefix: String, value: String):
  override def toString: String = prefix + value
