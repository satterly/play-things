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
    Ok(Json.obj("data" -> things))
  }

  def show(id: Long) = Action {

    Thing.findById(id).map { thing =>
      Ok(Json.obj(
        "data" -> Json.obj(
          "thing" -> Thing.rate(1, Thing.rate(5, thing))
        )
      ))
    }.getOrElse(NotFound)
  }

  def create = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def update(id: Long) = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def delete(id: Long) = Action {
    Ok(views.html.index("Your new application is ready."))
  }
}
