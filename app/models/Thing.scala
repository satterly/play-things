package models

import java.util.UUID

import org.elasticsearch.node.NodeBuilder._
import org.elasticsearch.common.settings.ImmutableSettings._
import org.joda.time.{DateTimeZone, DateTime}
import org.joda.time.format.ISODateTimeFormat
import play.api.libs.json.{Json, JsString, JsValue, Writes}


case class Location (
  latitude: Double,
  longitude: Double,
  altitude: Double)

object Location {
  implicit val locationWrites = Json.writes[Location]
}

case class Thing (
  id: Option[String] = Some(UUID.randomUUID.toString),
  note: String,
  link: String,
  `type`: String,
  userId: Int,
  status: String,
  isPublic: Boolean,
  rating: Double,
  votes: Int,
  tags: Seq[String],
  location: Location,
  image: String,
  createdAt: DateTime = new DateTime(),
  lastModified: DateTime = new DateTime()) {

  def rate(rating: Int): Thing = {
    this.copy(
      rating = Math.ceil((this.rating * 10 * this.votes + rating * 10) / (this.votes + 1)) / 10,
      votes = this.votes + 1
    )
  }
}

object Thing {

  implicit val dateTimeWrites = new Writes[DateTime] {
    def writes(t: DateTime): JsValue = JsString(ISODateTimeFormat.dateTime.withZone(DateTimeZone.UTC).print(
      new DateTime(t))
    )
  }

  implicit val thingWrites = Json.writes[Thing]

  lazy val indexName = "things"
  lazy val typeName = "thing"

  val node = nodeBuilder.client(true).node
  val client = node.client

  val thing1 = Thing(
    note = "title",
    link = "http://",
    `type` = "bike",
    userId = 5,
    status = "visible",
    isPublic = false,
    rating = 4,
    votes = 1,
    tags = Seq("foo", "bar", "baz"),
    location = Location(444.5, 3456.0, 100),
    image = "http://")

  val thing2 = Thing(
    note = "title",
    link = "http://",
    `type` = "bike",
    userId = 5,
    status = "visible",
    isPublic = false,
    rating = 4,
    votes = 1,
    tags = Seq("foo", "bar", "baz"),
    location = Location(444.5, 3456.0, 100),
    image = "http://")

  def search(q: String): Seq[Thing] = {

    println(s"Searching all things for $q")
    Seq(thing1)
  }

  def all: List[Thing] = {
    println("Returning list of all things")
    List(thing1, thing2)
  }

  def findById(id: Long): Option[Thing] = {

    Some(thing1)
  }

  def findByTag(tag: String): Seq[Thing] = {
    println(s"Filtering by tag $tag")
    Seq(thing2)
  }

  def create(note: String, link: String, `type`: String, userId: Int, status: String, isPublic: Boolean, rating: Double,
             votes: Int, tags: Seq[String], location: Location, image: String): Option[Thing] = {

    val thing = Thing(note = note, link = link, `type` = `type`, userId = userId, status = status, isPublic = isPublic,
      rating = rating, votes = votes, tags = tags, location = location, image = image)

    client.prepareIndex(indexName, typeName, thing.id.get)
      .setSource(Json.toJson(thing).toString)
      .execute()
      .actionGet()
    Some(thing)
  }

  def update(id: Long, thing: Thing): Boolean = ???

  def delete(id: Long): Boolean = ???
}
