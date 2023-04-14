package controllers

import play.api.libs.json._
import play.api.mvc._

import javax.inject._

case class Item(id: Long, name: String, categoryId: Long)

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class ItemController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  implicit val json = Json.format[Item]
  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def getItems() = Action { implicit request: Request[AnyContent] =>
    Ok(Json.toJson(Database.items))
  }

  def getItemById(itemId: Long) = Action { implicit request: Request[AnyContent] =>
    val itemFromDB = Database.items.filter(item => item.id == itemId)
    if (itemFromDB.isEmpty) {
      NotFound("Item not found")
    }
    else {
      Ok(Json.toJson(itemFromDB.head))
    }
  }

  def addItem(itemId: Long, name: String, categoryId: Long) = Action { implicit request: Request[AnyContent] =>
    val itemExists = Database.items.exists(item => item.id == itemId)
    val categoryExists = Database.categories.exists(category => category.id == categoryId)
    if (itemExists) {
      NotAcceptable("Item with given id already exists")
    }
    else if (!categoryExists) {
      NotAcceptable("Category with given id does not exist")
    }
    else {
      val item = Item(itemId, name, categoryId)
      Database.items += item
      Ok(Json.toJson(item))
    }
  }

  def updateItem(itemId: Long, name: String, categoryId: Long) = Action { implicit request: Request[AnyContent] =>
    val itemFromDB = Database.items.filter(item => item.id == itemId)
    val categoryExists = Database.categories.exists(category => category.id == categoryId)
    if (itemFromDB.isEmpty) {
      NotFound("Item not found")
    }
    else if (!categoryExists) {
      NotAcceptable("Category with given id does not exist")
    }
    else {
      Database.items -= itemFromDB.head
      val updatedItem = Item(itemFromDB.head.id, name, categoryId)
      Database.items += updatedItem
      Ok(Json.toJson(updatedItem))
    }
  }

  def deleteItem(itemId: Long) = Action { implicit request: Request[AnyContent] =>
    val itemFromDB = Database.items.filter(item => item.id == itemId)
    if (itemFromDB.isEmpty) {
      NotFound("Item not found")
    }
    else {
      Database.items -= itemFromDB.head
      Ok(Json.toJson(itemFromDB))
    }
  }
}

