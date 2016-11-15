package com.akka.cluster.api.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server._

import scala.concurrent.Future
import scala.util.{Failure, Try}

private [routes] trait Results extends Directives {

  protected def onCompleteWithHandling[T](f: => Future[T])(implicit tryHandler: PartialFunction[Try[T],StandardRoute]): Route = {
    onComplete(f)(tryHandler orElse commonFailures)
  }

  private val commonFailures: PartialFunction[Try[_], StandardRoute] = {
//    case Failure(UserNotAuthorized(usr)) => complete(StatusCodes.Forbidden, forbiddenUserMessage(usr))
//    case Failure(UpdateFailed(id)) => complete(StatusCodes.InternalServerError, updateFailedMessage(id))
//    case Failure(NotFound(id)) => complete(StatusCodes.NotFound, notFoundMessage(id))
    case Failure(ex) => complete(StatusCodes.InternalServerError, ex.toString)
  }

}
