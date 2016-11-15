package com.akka.cluster.persistence.services

import akka.actor.Actor
import com.akka.cluster.persistence.dao.BaseDao
import com.akka.cluster.persistence.services.protocol.Save
import com.akka.cluster.persistence.util.PersistenceConfig
import akka.pattern.pipe
import scala.concurrent.Future

class UserPersistenceActor extends BasePersistenceActor(new BaseDao {}) {

  implicit val ec = context.dispatcher

  val partnersName = PersistenceConfig.userPartnersName
  private val r = new java.util.Random()

  override val applyProtocol: Actor.Receive = {
    case Save =>
      log.info("Save Mocked data")
      Thread.sleep(1000L)
      handleDummieFuture() pipeTo sender

  }

  private def handleDummieFuture(): Future[Int] = Future.successful(r.nextInt)

}
