package com.akka.cluster.services

import akka.actor.{Actor, ActorLogging, DeadLetter, Props}

class DeadLettersCatcher extends Actor with ActorLogging {

  override def receive: Receive = {
    case DeadLetter(msg, sdr, rcpt) =>
      log.warning(s"You have dead letters message: $msg , sender: $sdr and recipient: $rcpt")
  }

}

object DeadLettersCatcher {
  def props = Props(classOf[DeadLettersCatcher])
}
