package com.akka.cluster.services

import akka.actor.{Actor, ActorLogging, ActorRef, Terminated}
import akka.routing._
import com.akka.cluster.api.request.UserRequest
import com.akka.cluster.persistence.services.protocol._
import com.akka.cluster.services.UserServiceActor._

class UserServiceActor extends Actor with ActorLogging {

  private val userPersistenceActors = scala.collection.immutable.IndexedSeq.empty[Routee]
  //TODO IMPLEMENT ROUTER
//  private var counter = 0

  var router = Router(SmallestMailboxRoutingLogic(), userPersistenceActors)
//  var router = Router(RoundRobinRoutingLogic(), userPersistenceActors)

  override def receive: Actor.Receive = {
    case CreateUser(_) if router.routees.isEmpty =>
      log.warning("Don't found any user persistence actor...")
      sender() ! TaskFailed("Service unavailable")
    case msg@CreateUser(r) =>
//      counter += 1
      router.route(Save, sender())
//      userPersistenceActors(counter % userPersistenceActors.size) forward Save //msg
    case RegisteredNode if !userPersistenceActors.contains(sender()) =>
      context watch sender()
      log.debug("Actor registered: " + sender().path)
      router = router.addRoutee(sender())
//      userPersistenceActors = userPersistenceActors :+ sender()
    case Terminated(a) =>
      log.debug("Removing actor {}", a)
      router = router.removeRoutee(a)
//      userPersistenceActors = userPersistenceActors.filterNot(_ == a)
    case UnRegisteredNode =>
      log.debug("Removing actor {}", sender())
      router = router.removeRoutee(sender())
//      userPersistenceActors = userPersistenceActors.filterNot(_ == sender())
  }

}

object UserServiceActor {

  case class CreateUser(request: UserRequest)

}
