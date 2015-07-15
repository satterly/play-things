package controllers

import models.Thing
import play.api.Play
import play.api.libs.json._
import play.api.libs.ws._
import play.api.mvc._

class Things extends Controller {

  def list = Action { implicit request =>

    val query = request.getQueryString("q").getOrElse("")
    val tag = request.getQueryString("tag").getOrElse("")

    val things = if (!query.isEmpty) {
      Thing.search(query).toList
    } else if (!tag.isEmpty) {
      Thing.findByTag(tag).toList
    } else {
      Thing.findAll
    }
    Ok(Json.obj(
      "uri" -> s"http://localhost:9000/things?${request.rawQueryString}",
      "data" -> things,
      "total" -> things.length
    ))
  }

  def show(id: String) = Action {

    Thing.findById(id).map { thing =>
      Ok(Json.obj(
        "uri" -> s"http://localhost:9000/things/$id",
        "data" -> thing.rate(1)
      ))
    }.getOrElse(NotFound)
  }

  def create = Action(parse.json) { request =>
    Thing.fromJson(request.body).map { thing =>
      Thing.save(thing).map { result =>
        Created(Json.obj(
          "uri" -> s"http://localhost:9000/things/${thing.id}",
          "data" -> thing
        ))
      }.getOrElse(InternalServerError)
    }.getOrElse(BadRequest)
  }

  def update(id: String) = Action(parse.json) { request =>
    Thing.update(id, request.body)
    Ok
  }

  def remove(id: String) = Action {
    Thing.deleteById(id)
    Ok
  }

  def delete = Action { implicit request =>
    val tag = request.getQueryString("tag").getOrElse("")
    if (!tag.isEmpty) {
      Thing.deleteByTag(tag)
    }
    Ok
  }

}
