package com.akka.cluster.utils

import com.typesafe.config.ConfigFactory

trait Config {

  private val config = ConfigFactory.load()
  private val httpConfig = config.getConfig("http")

  val userTimeout = config.getInt("services.user.timeout")

  val httpInterface = httpConfig.getString("interface")
  val httpPort = httpConfig.getInt("port")


}
