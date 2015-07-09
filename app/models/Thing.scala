package models

import java.sql.Timestamp

import org.joda.time.{DateTimeZone, DateTime}
import org.joda.time.format.ISODateTimeFormat
import play.api.libs.json.{Json, JsString, JsValue, Writes}

case class Thing (
  id: Option[Long] = None,
  title: String,
  description: String,
  `type`: String,
  userId: Int,
  status: String,
  rating: Int,
  tags: Seq[String],
  geocode: Map[String, String],
  thumbnail: String,
  createdAt: DateTime = new DateTime(),
  lastModified: DateTime = new DateTime())

object Thing {

  implicit val dateTimeWrites = new Writes[DateTime] {
    def writes(t: DateTime): JsValue = JsString(ISODateTimeFormat.dateTime.withZone(DateTimeZone.UTC).print(
      new DateTime(t))
    )
  }

  implicit val thingWrites = Json.writes[Thing]

  def search(q: String): Seq[Thing] = {

    println(s"Searching all things for $q")
    Seq()
  }

  def all: List[Thing] = {
    println("Returning list of all things")
    List()
  }

  def findById(id: Long): Option[Thing] = {

    Some(Thing(Some(1L), "title", "desc", "bike", 5, "visible", 4, Seq("foo", "bar", "baz"), Map("lat" -> "44", "lng" -> "3345"), "http://"))
  }

  def findByTag(tag: String): Seq[Thing] = {
    println(s"Filtering by tag $tag")
    Seq()
  }

  def create(name: String, note: String, status: String): Option[Thing] = ???

  def update(id: Long, thing: Thing): Boolean = ???

  def delete(id: Long): Boolean = ???
}
