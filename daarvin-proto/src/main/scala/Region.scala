import akka.actor.{Actor, ActorRef}

import scala.concurrent.Future
import scala.reflect.ClassTag
import scala.util.{Failure, Random, Success}

abstract class Region extends Actor with Config with RegionBehavior with RequiresAgent {

  val children = collection.mutable.Map[ActorRef, AgentInfo]()

  override def preStart() =
    (1 to regionAgents).foreach { _ => context.actorOf(agentProps()) }

  def receive = {
    case MeetingRequest(energy) =>
      children += { (sender, MeetingInfo(sender, energy)) }
      processPair (meetingAction)
    case CrossOverRequest(gen) =>
      children += { (sender, CrossOverInfo(sender, gen)) }
      processPair (crossOverAction)
  }

  private def processPair[AI <: AgentInfo : ClassTag](action: (AI, AI) => Unit) =
    drawPairInfo[AI].foreach { case (infoA, infoB) =>
      action(infoA, infoB)
      children -= (infoA.ref, infoB.ref)
    }

  private def drawPairInfo[AI <: AgentInfo : ClassTag] = {
    val byType = children.values.collect { case v: AI => v }
    val draw = Random.shuffle(byType).take(2).toSeq
    if (draw.length == 2) Some((draw(0), draw(1))) else None
  }

  private def meetingAction(agentA: MeetingInfo, agentB: MeetingInfo) = {
    import context.dispatcher

    Future {
      val energyA = meetingEnergyUpdate(agentA.energy, agentB.energy)
      val energyB = meetingEnergyUpdate(agentB.energy, agentA.energy)
      (energyA, energyB)
    }.onComplete {
      case Success((energyA, energyB)) =>
        agentA.ref ! EnergyUpdate(energyA)
        agentB.ref ! EnergyUpdate(energyB)
      case Failure(_) =>
        agentA.ref ! EnergyUpdateFailed
        agentB.ref ! EnergyUpdateFailed
    }
  }

  private def crossOverAction(agentA: CrossOverInfo, agentB: CrossOverInfo) = {
    import context.dispatcher

    Future {
      val gen = crossOver(agentA.gen, agentB.gen)
      context.actorOf(agentProps(Some(gen)))
    }.onComplete {
      case Success(_) =>
        agentA.ref ! CrossOverSucceeded
        agentB.ref ! CrossOverSucceeded
      case Failure(_) =>
        agentA.ref ! CrossOverFailed
        agentB.ref ! CrossOverFailed
    }
  }

}
