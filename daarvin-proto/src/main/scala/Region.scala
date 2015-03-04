import akka.actor.{Actor, ActorLogging, ActorRef, ReceiveTimeout}
import utils.LazyLog

import scala.concurrent.Future
import scala.concurrent.duration.Duration
import scala.reflect.ClassTag
import scala.util.{Failure, Random, Success}

abstract class Region[G : ClassTag, E <: Expandable[E] with Contractive[E], F <: Ordered[F] : ClassTag]
  extends Actor with ActorLogging with LazyLog with Config with RegionBehavior
  with RequiresMetrics with RequiresAgent[G] with AgentHallmarks {

  type Gen = G
  type Energy = E
  type Fit = F

  import context.dispatcher

  val children = collection.mutable.Map[ActorRef, AgentInfo]()

  val metricsHub = context.actorOf(metricsProps, "metrics-hub")
  val noOfMetricsHubs = 1

  (1 to regionAgents).foreach {
    num => context.actorOf(agentProps(metricsHub))
  }

  var bestSolution: (Gen, Fit)

  var pendingSolutions: Option[Int] = None

  var recipient: Option[ActorRef] = None

  context.system.scheduler.scheduleOnce(operatingTime) {
    llog.info("Region finish issued")
    self ! Finish
  }

  llog.info("Starting region")

  def receive = {
    case RegRecipient =>
      recipient = Some(sender)

    case req: MeetingRequest[F] =>
      llog.debug(s"Meeting request received from $sender")
      children += { (sender, MeetingInfo(sender, req.fitness)) }
      processPair (meetingAction)

    case req: CrossOverRequest[G] =>
      llog.debug(s"Cross-over request received from $sender")
      children += { (sender, CrossOverInfo(sender, req.gen)) }
      processPair (crossOverAction)

    case Finish =>
      llog.debug("Finish received")
      pendingSolutions = Some(context.children.size - noOfMetricsHubs)
      context.children.foreach { ch => ch ! Finish }
      resetTimeout

    case sol: Solution[G, F] =>
      processSolution(sol.gen, sol.fitness)
      pendingSolutions = pendingSolutions map (_ - 1)
      llog.debug(s"Received solution $sol (${pendingSolutions.getOrElse(0)} pending)")
      pendingSolutions.map { pending =>
        if (pending > 0)
          resetTimeout
        else {
          timeoutOff
          gatherMetrics
        }
      }

    case ReceiveTimeout =>
      llog.debug("Timeout received")
      timeoutOff
      gatherMetrics

    case metrics: MetricsData =>
      llog.debug("Sending best solution and metrics")
      recipient.map { _ ! (bestSolution, metrics) }
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

  private def meetingAction(agentA: MeetingInfo[Fit], agentB: MeetingInfo[Fit]) = {
    def toMsg(better: Boolean) = if (better) EnergyGained else EnergyLost

    Future {
      val changeForA = toMsg { hasBetterFitness(agentA.fitness, agentB.fitness) }
      val changeForB = toMsg { hasBetterFitness(agentB.fitness, agentA.fitness) }
      (changeForA, changeForB)
    }.onComplete {
      case Success((changeForA, changeForB)) =>
        llog.debug(s"Meeting succeeded: agents ${agentA.ref} and ${agentB.ref}")
        agentA.ref ! changeForA
        agentB.ref ! changeForB
        metricsHub ! MeetingRecord

      case Failure(_) =>
        llog.debug(s"Meeting failed: agents ${agentA.ref} and ${agentB.ref}")
        agentA.ref ! MeetingFailed
        agentB.ref ! MeetingFailed
    }
  }

  private def crossOverAction(agentA: CrossOverInfo[Gen], agentB: CrossOverInfo[Gen]) =
    Future {
      val gen = crossOver(agentA.gen, agentB.gen)
      context.actorOf(agentProps(metricsHub, Some(gen)))
    }.onComplete {
      case Success(_) =>
        llog.debug(s"Cross-over succeeded: agents ${agentA.ref} and ${agentB.ref}")
        agentA.ref ! CrossOverSucceeded
        agentB.ref ! CrossOverSucceeded
        metricsHub ! CrossOverRecord

      case Failure(_) =>
        llog.debug(s"Cross-over failed: agents ${agentA.ref} and ${agentB.ref}")
        agentA.ref ! CrossOverFailed
        agentB.ref ! CrossOverFailed
    }

  def processSolution(gen: Gen, fitness: Fit) = {
    val (_, bestFit) = bestSolution
    if (fitness > bestFit)
      bestSolution = (gen, fitness)
  }

  private def resetTimeout = context.setReceiveTimeout(finishTimeout)

  private def timeoutOff = context.setReceiveTimeout(Duration.Undefined)

  private def gatherMetrics = metricsHub ! MetricsRequest

}
