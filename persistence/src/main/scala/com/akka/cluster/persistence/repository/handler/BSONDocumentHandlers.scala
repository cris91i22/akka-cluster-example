package com.akka.cluster.persistence.repository.handler

import java.sql.Date

import com.akka.cluster.persistence.model.User
import org.joda.time.{DateTime, DateTimeZone, LocalDate}
import reactivemongo.bson.{BSONDateTime, BSONHandler, DefaultBSONHandlers, Macros}

object BSONDocumentHandlers extends DefaultBSONHandlers {

  val BuenosAiresTimeZone = "America/Argentina/Buenos_Aires"

  implicit object JavaDateBSONHandler extends BSONHandler[BSONDateTime, Date] {
    def read(bsonDateTime: BSONDateTime) = new Date(bsonDateTime.value)
    def write(date: Date) = BSONDateTime(date.getTime)
  }

  implicit object BSONJodaDateTimeBSONHandler extends BSONHandler[BSONDateTime, DateTime] {
    def read(bsonDateTime: BSONDateTime) = new DateTime(bsonDateTime.value).withZone(DateTimeZone.forID(BuenosAiresTimeZone))
    def write(dateTime: DateTime) = BSONDateTime(dateTime.getMillis)
  }

  implicit val userHandler = Macros.handler[User]

}
