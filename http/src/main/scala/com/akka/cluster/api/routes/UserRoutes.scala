package com.akka.cluster.api.routes

import akka.actor.ActorRef
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.StandardRoute
import akka.pattern.ask
import akka.util.Timeout
import com.akka.cluster.api.request.UserRequest
import com.akka.cluster.services.UserServiceActor.CreateUser
import com.akka.cluster.utils.AutoMarshaller
import scala.util.{Success, Try}

trait UserRoutes extends AutoMarshaller with Results {

  val userService: ActorRef
  implicit val userServiceTimeout: Timeout

  val create = (pathEnd & get) {
    entity(as[UserRequest]) { record =>
      implicit val tryHandler: PartialFunction[Try[Int], StandardRoute] = {
        case Success(r) => complete(Created, Map("id" -> r))
      }
      onCompleteWithHandling((userService ? CreateUser(record)).mapTo[Int])
    }
  }

//  val retrieve = (path(Segment) & get) { id =>
//    onComplete(userService ? protocol.Save) {
//      case Success(v) => v match {
//        case x: Int => complete(StatusCodes.OK, Map("id" -> x))
//        case TaskFailed(msg) => complete(StatusCodes.BadRequest, Map("error" -> msg))
//      }
//      case Failure(ex) => complete(BadRequest)
//    }
//
//  }

  val userRoutes = pathPrefix("user"){
    create //~
      //retrieve
  }

}
