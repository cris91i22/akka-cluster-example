package com.akka.cluster.services

import akka.actor.{Actor, ActorLogging, Terminated}
import akka.routing.{Routee, Router}
import com.akka.cluster.persistence.services.protocol.{RegisteredNode, UnRegisteredNode}

import scala.collection.immutable.IndexedSeq

abstract class BaseRouterActor extends Actor with ActorLogging {

  protected var router: Router

  protected val userPersistenceActors: IndexedSeq[Routee]

  protected def applyProtocol: Actor.Receive

  override def receive: Actor.Receive = basicProtocol orElse applyProtocol

  val basicProtocol: Actor.Receive = {
    case RegisteredNode if !router.routees.contains(sender()) =>
      context watch sender()
      log.debug("Actor registered: " + sender().path)
      router = router.addRoutee(sender())
    case Terminated(a) =>
      log.debug("Removing actor {}", a)
      router = router.removeRoutee(a)
    case UnRegisteredNode =>
      log.debug("Removing actor {}", sender())
      router = router.removeRoutee(sender())
  }


}
