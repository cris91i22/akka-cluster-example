package com.akka.cluster.api.routes

import akka.actor.ActorRef
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.StandardRoute
import akka.pattern.ask
import akka.util.Timeout
import com.akka.cluster.api.request.UserRequest
import com.akka.cluster.persistence.model.User
import com.akka.cluster.services.UserServiceActor.{CreateUser, GetUser}
import com.akka.cluster.utils.AutoMarshaller
import reactivemongo.bson.BSONObjectID

import scala.util.{Success, Try}

trait UserRoutes extends AutoMarshaller with Results {

  val userService: ActorRef
  implicit val userServiceTimeout: Timeout

  val create = (pathEnd & post) {
    entity(as[UserRequest]) { record =>
      implicit val tryHandler: PartialFunction[Try[BSONObjectID], StandardRoute] = {
        case Success(r) => complete(Created, Map("id" -> r))
      }
      onCompleteWithHandling((userService ? CreateUser(record)).mapTo[BSONObjectID])
    }
  }

  val retrieve = (path(Segment) & get) { id =>
    implicit val tryHandler: PartialFunction[Try[Option[User]], StandardRoute] = {
      case Success(Some(r)) => complete(OK, r)
      case Success(None) => complete(NotFound, s"Not found entity with id $id")
    }
    onCompleteWithHandling((userService ? GetUser(id)).mapTo[Option[User]])
  }

  val userRoutes = pathPrefix("user"){
    create ~
      retrieve
  }

}
