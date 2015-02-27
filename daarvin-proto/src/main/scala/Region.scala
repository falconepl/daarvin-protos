import akka.actor.{Actor, ActorRef, ReceiveTimeout}

import scala.concurrent.Future
import scala.concurrent.duration.Duration
import scala.reflect.ClassTag
import scala.util.{Failure, Random, Success}

abstract class Region extends Actor with Config with RegionBehavior with RequiresMetrics with RequiresAgent {

  val children = collection.mutable.Map[ActorRef, AgentInfo]()

  val metricsHub = context.actorOf(metricsProps)

  (1 to regionAgents).foreach {
    _ => context.actorOf(agentProps(sender, metricsHub))
  }

  var bestSolution = (IndexedSeq.empty[Int], 0)

  var pendingSolutions: Option[Int] = None

  var recipient: Option[ActorRef] = None

  def receive = {
    case RegRecipient =>
      recipient = Some(sender)

    case MeetingRequest(fitness) =>
      children += { (sender, MeetingInfo(sender, fitness)) }
      processPair (meetingAction)

    case CrossOverRequest(gen) =>
      children += { (sender, CrossOverInfo(sender, gen)) }
      processPair (crossOverAction)

    case Finish =>
      pendingSolutions = Some(context.children.size)
      context.children.foreach { ch => ch ! Finish }
      resetTimeout

    case Solution(gen, fitness) =>
      processSolution(gen, fitness)
      pendingSolutions = pendingSolutions map (_ - 1)
      pendingSolutions.map { pending =>
        if (pending > 0)
          resetTimeout
        else {
          timeoutOff
          gatherMetrics
        }
      }

    case ReceiveTimeout =>
      timeoutOff
      gatherMetrics

    case Metrics(metricsData) =>
      recipient.map { _ ! (bestSolution, metricsData) }
      context stop self
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

    def toMsg(better: Boolean) = if (better) EnergyGained else EnergyLost

    Future {
      val changeForA = toMsg { hasBetterFitness(agentA.fitness, agentB.fitness) }
      val changeForB = toMsg { hasBetterFitness(agentB.fitness, agentA.fitness) }
      (changeForA, changeForB)
    }.onComplete {
      case Success((changeForA, changeForB)) =>
        agentA.ref ! changeForA
        agentB.ref ! changeForB
        metricsHub ! MeetingRecord

      case Failure(_) =>
        agentA.ref ! MeetingFailed
        agentB.ref ! MeetingFailed
    }
  }

  private def crossOverAction(agentA: CrossOverInfo, agentB: CrossOverInfo) = {
    import context.dispatcher

    Future {
      val gen = crossOver(agentA.gen, agentB.gen)
      context.actorOf(agentProps(sender, metricsHub, Some(gen)))
    }.onComplete {
      case Success(_) =>
        agentA.ref ! CrossOverSucceeded
        agentB.ref ! CrossOverSucceeded
        metricsHub ! CrossOverRecord

      case Failure(_) =>
        agentA.ref ! CrossOverFailed
        agentB.ref ! CrossOverFailed
    }
  }

  def processSolution(gen: IndexedSeq[Int], fitness: Int) = {
    val (_, bestFit) = bestSolution
    if (fitness > bestFit)
      bestSolution = (gen, fitness)
  }

  private def resetTimeout = context.setReceiveTimeout(finishTimeout)

  private def timeoutOff = context.setReceiveTimeout(Duration.Undefined)

  private def gatherMetrics = metricsHub ! MetricsRequest

}
