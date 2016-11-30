package com.akka.cluster.persistence.model

import org.joda.time.DateTime
import reactivemongo.bson.BSONObjectID

trait Entity {
  //  @Key("_id")
  val _id: BSONObjectID
}

case class User(_id: BSONObjectID,
                userId: String,
                name: String,
                dateOfBirth: DateTime) extends Entity