package service

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.effect.IO.*
import domain.Category

object IOService{
  def println(string: String): Unit = {
    IO.println(string).unsafeRunSync()
  }
  def readLine: String = {
    IO.readLine.unsafeRunSync()
  }

  def askAndRead[A](question: String, parser: String => A = identity): IO[A] = {
    for {
      _ <- IO.println(question)
      input <- IO.readLine
    } yield parser(input)
  }

  def parseCategory(input: String): Category = {
    val categoryIndex = input.toInt - 1
    Category.fromOrdinal(categoryIndex)
  }
}
