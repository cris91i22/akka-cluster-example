package com.akka.cluster.persistence.repository.config

import com.typesafe.config.ConfigFactory
import scala.collection.JavaConverters._

trait DbConfig {

  private val db = ConfigFactory.load().getConfig("db")
  private val hosts = db.getStringList("hosts").asScala
  private val port = db.getInt("port")
  private val databaseName = db.getString("dataBaseName")

  val mongoUri = s"mongodb://${hosts.map(h => s"$h:$port").mkString(",")}/$databaseName"

}