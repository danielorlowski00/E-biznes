package controllers

import scala.collection.mutable

object Database {
  val users = new mutable.ListBuffer[User]()
  val items = new mutable.ListBuffer[Item]()
  val categories = new mutable.ListBuffer[Category]()
}
