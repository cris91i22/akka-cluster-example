package com.akka.cluster

import akka.actor.{ActorSystem, DeadLetter, Props}
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.akka.cluster.api.routes.Routes
import com.akka.cluster.services.{DeadLettersCatcher, UserServiceActor}
import com.akka.cluster.utils.Config
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration._

object Main extends App with Config with Routes {
  implicit lazy val userServiceTimeout = Timeout(userTimeout seconds)

  lazy val config = ConfigFactory.parseString("akka.cluster.roles = [userfrontend]").
    withFallback(ConfigFactory.load())

  implicit lazy val system = ActorSystem("AkkaClusterSystem", config)
  lazy val log = Logging(system, getClass)
  implicit lazy val executor = system.dispatcher
  implicit lazy val materializer = ActorMaterializer()

  // Services
  val userService = system.actorOf(Props[UserServiceActor], name = "userfrontend")

  // Dead letters catcher
  val deadLettersCatcher = system.actorOf(DeadLettersCatcher.props, "dead-letters-catcher")
  system.eventStream.subscribe(deadLettersCatcher, classOf[DeadLetter])

  // Server
  Http().bindAndHandle(handler = logResult("log")(routes), interface = httpInterface, port = httpPort)
}
