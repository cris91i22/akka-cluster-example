package com.akka.cluster.services

import akka.routing._
import com.akka.cluster.api.request.UserRequest
import com.akka.cluster.persistence.services.protocol._
import com.akka.cluster.services.UserServiceActor._

import scala.collection.immutable.IndexedSeq

class UserServiceActor extends BaseRouterActor {

  override protected val userPersistenceActors: IndexedSeq[Routee] = IndexedSeq.empty

  // DOC in: http://doc.akka.io/docs/akka/2.4.10/scala/routing.html
  override var router = Router(RoundRobinRoutingLogic(), userPersistenceActors)

  override protected def applyProtocol: Receive = {
    case _ if router.routees.isEmpty => log.warning("Don't found any user persistence actor...")
      sender() ! TaskFailed("Service unavailable")
    case msg@CreateUser(r) => router.route(Save, sender())
    case GetUser(id) => router.route(Get(id), sender())
  }

}

object UserServiceActor {

  case class CreateUser(request: UserRequest)
  case class GetUser(id: String)

}
