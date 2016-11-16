package com.akka.cluster.services

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorLogging, ActorRef, Terminated}
import akka.routing._
import com.akka.cluster.api.request.UserRequest
import com.akka.cluster.persistence.services.protocol._
import com.akka.cluster.services.UserServiceActor._

import scala.collection.immutable.IndexedSeq

class UserServiceActor extends BaseRouterActor {

  override protected val userPersistenceActors: IndexedSeq[Routee] = IndexedSeq.empty

  // DOC in: http://doc.akka.io/docs/akka/2.4.10/scala/routing.html
  override var router = Router(RoundRobinRoutingLogic(), userPersistenceActors)
// override var router = Router(SmallestMailboxRoutingLogic(), userPersistenceActors)

  override protected def applyProtocol: Receive = {
    case CreateUser(_) if router.routees.isEmpty =>
      log.warning("Don't found any user persistence actor...")
      sender() ! TaskFailed("Service unavailable")
    case msg@CreateUser(r) =>
      router.route(Save, sender())
  }

//  override def receive: Actor.Receive = {
//    case CreateUser(_) if router.routees.isEmpty =>
//      log.warning("Don't found any user persistence actor...")
//      sender() ! TaskFailed("Service unavailable")
//    case msg@CreateUser(r) =>
////      counter += 1
//      router.route(Save, sender())
////      userPersistenceActors(counter % userPersistenceActors.size) forward Save //msg
//    case RegisteredNode if !userPersistenceActors.contains(sender()) =>
//      context watch sender()
//      log.debug("Actor registered: " + sender().path)
//      router = router.addRoutee(sender())
////      userPersistenceActors = userPersistenceActors :+ sender()
//    case Terminated(a) =>
//      log.debug("Removing actor {}", a)
//      router = router.removeRoutee(a)
////      userPersistenceActors = userPersistenceActors.filterNot(_ == a)
//    case UnRegisteredNode =>
//      log.debug("Removing actor {}", sender())
//      router = router.removeRoutee(sender())
////      userPersistenceActors = userPersistenceActors.filterNot(_ == sender())
//  }

}

object UserServiceActor {

  case class CreateUser(request: UserRequest)

}
