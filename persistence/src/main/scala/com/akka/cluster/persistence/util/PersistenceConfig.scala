package com.akka.cluster.persistence.util

import com.typesafe.config.ConfigFactory
import scala.collection.JavaConversions._

object PersistenceConfig {

  private val conf = ConfigFactory.load()

  val userPartnersName = conf.getStringList("actors.userPartnersName").toVector

}
