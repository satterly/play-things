package models

import java.util.UUID

import org.elasticsearch.node.NodeBuilder._
import org.elasticsearch.common.settings.ImmutableSettings._
import org.joda.time.{DateTimeZone, DateTime}
import org.joda.time.format.ISODateTimeFormat
import play.api.http.MediaType.parse
import play.api.libs.json._
import scala.collection.JavaConverters._

case class Location (
  latitude: Double,
  longitude: Double,
  altitude: Double)

object Location {
  implicit val locationFmt = Json.format[Location]
}

case class Thing (
  id: Option[String] = Some(UUID.randomUUID.toString),
  note: String,
  link: String,
  `type`: String,
  userId: Int,
  status: String = "new",
  isPublic: Boolean = false,
  rating: Double = 0.0,
  votes: Int = 0,
  tags: Seq[String] = Seq(),
  location: Option[Location],
  image: Option[String] = None
  //createdAt: Option[DateTime] = Some(new DateTime()),
  //lastModified: DateTime = new DateTime()
  ) {

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


  implicit val thingFmt = Json.format[Thing]

  lazy val indexName = "things"
  lazy val typeName = "thing"

  val node = nodeBuilder.client(true).node
  val client = node.client

  def search(q: String): Seq[Thing] = {

    println(s"Searching all things for $q")
    Seq()
  }

  def findAll: List[Thing] = {
    println("Returning list of all things")
    val response = client.prepareSearch(indexName)
      .setTypes(typeName)
      .execute()
      .actionGet()

    val thing = response.getHits.asScala.map { hit => Json.parse(hit.getSourceAsString).as[Thing] }
    thing.toList
  }

  def findById(id: Long): Option[Thing] = {

    None
  }

  def findByTag(tag: String): Seq[Thing] = {
    println(s"Filtering by tag $tag")
    Seq()
  }

  def save(thing: Thing): Option[Thing] = {
    println(thing.toString)
    client.prepareIndex(indexName, typeName, thing.id.get)
      .setSource(Json.toJson(thing).toString)
      .execute()
      .actionGet()
    Some(thing)
  }

  def update(id: Long, thing: Thing): Boolean = ???

  def delete(id: Long): Boolean = ???
}
