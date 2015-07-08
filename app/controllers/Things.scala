package controllers

import play.api._
import play.api.mvc._

class Things extends Controller {

  def find(q: Option[String]) = Action {

    q match {
      case Some(query) => println(s"search for $query")
      case _ => println("list all")
    }

    Ok(views.html.index("Your new application is ready."))
  }

  def show(id: Long) = Action {
//    Things.findById(id).map { client =>
//      Ok(views.html.Things.display(client))
//    }.getOrElse(NotFound)
    Ok(views.html.index("Your new application is ready."))
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

//  def search = Action {
//    Ok(views.html.index("Your new application is ready."))
//  }

}
