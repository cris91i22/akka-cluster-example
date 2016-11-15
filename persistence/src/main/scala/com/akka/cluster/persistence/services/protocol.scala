package com.akka.cluster.persistence.services

object protocol {
  case object Save
  case object Update
  case object Delete
  case object Ping
  final case class TaskFailed(msg: String)
  case object RegisteredNode
  case object UnRegisteredNode
  case object ShutDown
}
