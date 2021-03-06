package com.akka.cluster.persistence.services

object protocol {
  case object Save
  case class Get(id: String)
  final case class TaskFailed(msg: String)

  case object RegisteredNode
  case object UnRegisteredNode
  case object ShutDown

  case class UserResponse(id: String, name: String, genre: String)

}
