package com.akka.cluster.persistence.services

import akka.actor.{Actor, Props}
import akka.pattern.pipe
import com.akka.cluster.persistence.repository.UserRepository
import com.akka.cluster.persistence.model.User
import com.akka.cluster.persistence.services.protocol.{Get, Save}
import com.akka.cluster.persistence.util.PersistenceConfig
import org.joda.time.DateTime
import reactivemongo.bson.BSONObjectID
import com.akka.cluster.persistence.repository.handler.BSONDocumentHandlers._

class UserPersistenceActor(val repository: UserRepository) extends BasePersistenceActor {

  implicit val ec = context.dispatcher

  val partnersName = PersistenceConfig.userPartnersName
  private val r = new java.util.Random()

  override val applyProtocol: Actor.Receive = {
    case Save => repository.insert(User(BSONObjectID.generate(), "1", "a", DateTime.now)) pipeTo sender
    case Get(id) => repository.findOne(id) pipeTo sender
  }

}

object UserPersistenceActor {

  def props(repository: UserRepository): Props = {
    Props(classOf[UserPersistenceActor], repository)
  }

}
