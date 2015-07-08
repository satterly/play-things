package models

import org.joda.time.DateTime

case class Thing (
  id: Option[Long] = None,
                 title: String,
  note: String,
  createdAt: DateTime = new DateTime(),
  lastModified: DateTime = new DateTime(),
  status: String)

object Thing {

  def all: List[Thing] = ???

  def findById(id: Long): Option[Thing] = ???

  def search(q: String): Seq[Thing] = ???

  def update(id: Long, thing: Thing): Boolean = ???

  def create(name: String, note: String, status: String): Option[Thing] = ???

  def delete(id: Long): Boolean = ???
}
