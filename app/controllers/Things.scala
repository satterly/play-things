package controllers

import models.Thing
import play.api.Play
import play.api.libs.json._
import play.api.libs.ws._
import play.api.mvc._

class Things extends Controller {

  def find = Action { implicit request =>

    val query = request.getQueryString("q").getOrElse("")
    val tag = request.getQueryString("tag").getOrElse("")

    val things = if (!query.isEmpty) {
      Thing.search(query).toList
    } else if (!tag.isEmpty) {
      Thing.findByTag(tag).toList
    } else {
      Thing.all
    }
    Ok(Json.obj(
      "uri" -> s"http://localhost:9000/things?${request.rawQueryString}",
      "data" -> things,
      "total" -> things.length
    ))
  }

  def show(id: Long) = Action {

    Thing.findById(id).map { thing =>
      Ok(Json.obj(
        "uri" -> s"http://localhost:9000/things/$id",
        "data" -> thing.rate(1)
      ))
    }.getOrElse(NotFound)
  }

  def create = Action {


//    Thing.create( ).map { thing =>
//      Created(Json.obj("data" -> Json.obj("thing" -> thing)))
//    }.getOrElse(BadRequest)
    Ok
  }

  def update(id: Long) = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def delete(id: Long) = Action {
    Ok(views.html.index("Your new application is ready."))
  }
}
