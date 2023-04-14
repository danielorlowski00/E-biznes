package controllers

import play.api.libs.json._
import play.api.mvc._

import javax.inject._
import scala.collection.mutable

case class User(id: Long, name: String, surname: String, items: mutable.ListBuffer[Item])

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class UserController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  implicit val item = Json.format[Item]
  implicit val json = new Writes[User] {
    def writes(user: User) = Json.obj(
      "id" -> user.id,
            "name" -> user.name,
            "surname" -> user.surname,
            "items" -> user.items
    )
  }

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def getUsers() = Action { implicit request: Request[AnyContent] =>
    Ok(Json.toJson(Database.users))
  }

  def getUserById(userId: Long) = Action { implicit request: Request[AnyContent] =>
    val userFromDB = Database.users.filter(user => user.id == userId)
    if (userFromDB.isEmpty) {
      NotFound("user not found")
    }
    else {
      Ok(Json.toJson(userFromDB.head))
    }
  }

  def addUser(userId: Long, name: String, surname: String) = Action { implicit request: Request[AnyContent] =>
    val userExists = Database.users.exists(user => user.id == userId)
    if (userExists) {
      NotAcceptable("User with given id already exists")
    }
    else {
      val user = User(userId, name, surname, new mutable.ListBuffer[Item]())
      Database.users += user
      Ok(Json.toJson(user))
    }
  }

  def updateUser(userId: Long, name: String, surname: String) = Action { implicit request: Request[AnyContent] =>
    val userFromDB = Database.users.filter(user => user.id == userId)
    if (userFromDB.isEmpty) {
      NotFound("User not found")
    }
    else {
      Database.users -= userFromDB.head
      val updatedUser = User(userFromDB.head.id, name, surname, userFromDB.head.items)
      Database.users += updatedUser
      Ok(Json.toJson(updatedUser))
    }
  }

  def deleteUser(userId: Long) = Action { implicit request: Request[AnyContent] =>
    val userFromDB = Database.users.filter(user => user.id == userId)
    if (userFromDB.isEmpty) {
      NotFound("User not found")
    }
    else {
      Database.users -= userFromDB.head
      Ok(Json.toJson(userFromDB))
    }
  }

  def addItemToOrder(userId: Long, itemId: Long) = Action { implicit request: Request[AnyContent] =>
    val userFromDB = Database.users.filter(user => user.id == userId)
    val itemFromDB = Database.items.filter(item => item.id == itemId)
    if (userFromDB.isEmpty) {
      NotFound("User not found")
    }
    else if (itemFromDB.isEmpty) {
      NotFound("Item not found")
    }
    else {
      Database.users -= userFromDB.head
      userFromDB.head.items += itemFromDB.head
      Database.users += userFromDB.head
      Ok(Json.toJson(userFromDB.head))
    }
  }

  def deleteItemFromOrder(userId: Long, itemId: Long) = Action { implicit request: Request[AnyContent] =>
    val userFromDB = Database.users.filter(user => user.id == userId)
    val itemFromDB = Database.items.filter(item => item.id == itemId)
    if (userFromDB.isEmpty) {
      NotFound("User not found")
    }
    else if (itemFromDB.isEmpty) {
      NotFound("Item not found")
    }
    else {
      Database.users -= userFromDB.head
      userFromDB.head.items -= itemFromDB.head
      Database.users += userFromDB.head
      Ok(Json.toJson(itemFromDB.head))
    }
  }
}
