package com.akka.cluster.api.routes

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.RejectionHandler

trait Routes extends UserRoutes {

  val rh = RejectionHandler.newBuilder().handle{
    case rejection => complete(InternalServerError, rejection.toString)
  }.result()

  lazy val routes =
    handleRejections(rh) {
      userRoutes
    }

}
