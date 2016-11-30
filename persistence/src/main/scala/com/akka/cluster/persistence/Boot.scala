package com.akka.cluster.persistence

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.event.Logging
import com.akka.cluster.persistence.repository.UserRepository
import com.akka.cluster.persistence.services.UserPersistenceActor
import com.akka.cluster.persistence.services.protocol.ShutDown
import com.typesafe.config.{Config, ConfigFactory}

object Boot extends App {

  lazy val port = if (args.isEmpty) "0" else args(0)
  lazy val config: Config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port").
    withFallback(ConfigFactory.parseString("akka.cluster.roles = [userPersistence]")).
    withFallback(ConfigFactory.load())


  val system = ActorSystem("AkkaClusterSystem", config)
  val log = Logging(system, getClass)
  log.info("Remote config: " + config.getString("akka.remote.netty.tcp.port"))
  log.info("Remote Role: " + config.getList("akka.cluster.roles"))

  val actorsRegistration: Vector[ActorRef] = {
    Vector(system.actorOf(UserPersistenceActor.props(UserRepository()), name = "userPersistence"))
  }

  system.registerOnTermination(System.exit(-1))

  sys.addShutdownHook({
    log.info("Terminating node...")
    actorsRegistration foreach ( _ ! ShutDown)
  })

}
