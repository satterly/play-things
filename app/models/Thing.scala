package models

import java.util.UUID

import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.node.NodeBuilder._
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
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
  id: String = UUID.randomUUID.toString,
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
  image: Option[String] = None,
  createdAt: Option[DateTime] = Some(new DateTime()),
  lastModified: Option[DateTime] = Some(new DateTime())
  ) {

  def rate(rating: Int): Thing = {
    this.copy(
      rating = Math.ceil((this.rating * 10 * this.votes + rating * 10) / (this.votes + 1)) / 10,
      votes = this.votes + 1
    )
  }
}

object Thing {

  def fromJson(json: JsValue): Thing = {
    new Thing(
      id = UUID.randomUUID.toString,
      note = (json \ "note").as[String],
      link = (json \ "link").as[String],
      `type` = (json \ "type").as[String],
      userId = (json \ "userId").as[Int],
      status = (json \ "status").as[String],
      isPublic = (json \ "isPublic").as[Boolean],
      rating = (json \ "rating").as[Double],
      votes = 1,
      tags = (json \ "tags").as[Seq[String]],
      location = (json \ "location").asOpt[Location],
      image = (json \ "image").asOpt[String],
      createdAt = Some(new DateTime()),
      lastModified = Some(new DateTime())
    )
  }

  implicit object dateTimeWrites extends Writes[org.joda.time.DateTime] {
    def writes(d: DateTime): JsValue = JsString(ISODateTimeFormat.dateTime.withZoneUTC.print(d))
  }
  implicit val dateTimeReads = Reads.jodaDateReads("yyyy-MM-dd'T'HH:mm:ss.SSSZZ")

  implicit val thingFmt = Json.format[Thing]

  lazy val indexName = "things"
  lazy val typeName = "thing"

  val node = nodeBuilder.client(true).node
  val client = node.client

  def search(q: String): Seq[Thing] = {
    println(s"Searching all things for $q")
    val response = client.prepareSearch(indexName)
      .setTypes(typeName)
      .setQuery(QueryBuilders.queryStringQuery(q))
      .execute()
      .actionGet()

    val thing = response.getHits.asScala.map { hit => Json.parse(hit.getSourceAsString).as[Thing] }
    thing.toList
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
    val response = client.prepareSearch(indexName)
      .setTypes(typeName)
      .setQuery(QueryBuilders.termQuery("tags", tag))
      .execute()
      .actionGet()

    val thing = response.getHits.asScala.map { hit => Json.parse(hit.getSourceAsString).as[Thing] }
    thing.toList
  }

  def save(thing: Thing): Option[Thing] = {
    println(thing.toString)
    client.prepareIndex(indexName, typeName)
      .setSource(Json.toJson(thing).toString)
      .execute()
      .actionGet()
    Some(thing)
  }

  def update(id: Long, thing: Thing): Boolean = ???

  def delete(id: Long): Boolean = ???
}
