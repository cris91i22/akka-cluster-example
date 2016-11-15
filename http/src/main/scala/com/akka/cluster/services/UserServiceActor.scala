package com.akka.cluster.services

import akka.actor.{Actor, ActorLogging, ActorRef, Terminated}
import com.akka.cluster.api.request.UserRequest
import com.akka.cluster.persistence.services.protocol._
import com.akka.cluster.services.UserServiceActor._

class UserServiceActor extends Actor with ActorLogging {

  private var userPersistenceActors = IndexedSeq.empty[ActorRef] //TODO IMPLEMENT ROUTER
  private var counter = 0

  override def receive: Actor.Receive = {
    case CreateUser(_) if userPersistenceActors.isEmpty => sender() ! TaskFailed("Service unavailable")
    case msg@CreateUser(r) =>
      counter += 1
      userPersistenceActors(counter % userPersistenceActors.size) forward Save //msg
    case RegisteredNode if !userPersistenceActors.contains(sender()) =>
      context watch sender()
      log.debug("Actor registered: " + sender().path)
      userPersistenceActors = userPersistenceActors :+ sender()
    case Terminated(a) =>
      log.debug("Removing actor {}", a)
      userPersistenceActors = userPersistenceActors.filterNot(_ == a)
    case UnRegisteredNode =>
      log.debug("Removing actor {}", sender())
      userPersistenceActors = userPersistenceActors.filterNot(_ == sender())
  }

}

object UserServiceActor {

  case class CreateUser(request: UserRequest)

}
