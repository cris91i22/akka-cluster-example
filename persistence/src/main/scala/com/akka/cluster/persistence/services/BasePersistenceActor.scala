package com.akka.cluster.persistence.services

import akka.actor.{Actor, ActorLogging, RootActorPath}
import akka.cluster.ClusterEvent.{MemberUp, _}
import akka.cluster.{Cluster, Member, MemberStatus}
import com.akka.cluster.persistence.dao.BaseDao
import com.akka.cluster.persistence.services.protocol._

abstract class BasePersistenceActor(dao: BaseDao) extends Actor with ActorLogging {

  protected val cluster = Cluster(context.system)

  override def preStart(): Unit = cluster.subscribe( self, initialStateMode = InitialStateAsEvents,
    classOf[MemberEvent], classOf[UnreachableMember], classOf[MemberRemoved])

  override def postStop(): Unit = cluster.unsubscribe(self)

  def receive: Actor.Receive = basicProtocol orElse applyProtocol


  /**
    * List of front end actor which listen event from here
    */
  protected val partnersName: Vector[String]

  private val partner = (member: Member,name: String) =>
    context.actorSelection(RootActorPath(member.address) / "user" / name)

  /**
    * Callback method triggered every time a new member comes up
    *
    * @param member
    */
  private def register(member: Member): Unit = partnersName.foreach(p =>
    if (member.hasRole(p)) partner(member, p) ! RegisteredNode)

  /**
    * Callback method triggered every time a new member comes up
    *
    * @param member
    */
  private def unregister(member: Member): Unit = partnersName.foreach(p =>
    if (member.hasRole(p)) partner(member,p) ! UnRegisteredNode)


  /**
    * Actual implementation of message handling on given actor plus the basics
    */
  val applyProtocol: Actor.Receive


  /**
    * Basics about every cluster aware actor should do
    */
  private val basicProtocol: Actor.Receive = {
    case state: CurrentClusterState =>
      state.members.filter(_.status == MemberStatus.Up) foreach register
    case MemberUp(m) =>
      log.info("Member registered: " + m)
      register(m)

    case UnreachableMember(member) =>
      log.info("Member detected as unreachable: {}", member)
      cluster.down(member.address)
      unregister(member)

    case MemberRemoved(member, previousStatus) =>
      log.info("Member is Removed: {} after {}", member.address, previousStatus)
      cluster.down(member.address)
      unregister(member)

    case ShutDown =>
      log.info("Terminating member: {}" , cluster.selfRoles)
      cluster.down(cluster.selfAddress)
  }
}
