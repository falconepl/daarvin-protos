import akka.actor.{Actor, ActorLogging, ActorRef}
import math.Probabilities._
import utils.LazyLog

import scala.concurrent.Future

abstract class Agent[G](metricsHub: ActorRef, specificGen: Option[G] = None)
  extends Actor with ActorLogging with LazyLog with Config with AgentBehavior with Hallmarks {

  type Gen = G

  var energy = initEnergy

  var gen = specificGen getOrElse initGen

  var fitness = fitnessUpdate

  override def preStart() = controlled { talk }

  def receive = {
    case EnergyGained =>
      llog.debug("Energy gained")
      energy = energy + meetingCost
      controlled { tryMutate orElse talk }

    case EnergyLost =>
      llog.debug("Energy lost")
      energy = energy - meetingCost
      controlled { tryMutate orElse talk }

    case MeetingFailed =>
      llog.debug("Meeting failed")
      controlled { tryMutate orElse talk }

    case update: GenUpdate[G] =>
      llog.debug("Genetic update")
      gen = update.gen
      fitness = fitnessUpdate
      controlled { talk }
      metricsHub ! MutateRecord

    case CrossOverSucceeded =>
      llog.debug("Cross-over succeeded")
      energy = crossOverEnergyUpdate
      controlled { tryMutate orElse talk }

    case CrossOverFailed =>
      llog.debug("Cross-over failed")
      controlled { tryMutate orElse talk }

    case Finish =>
      llog.debug("Finish received")
      context.parent ! Solution(gen, fitness)
      die
  }

  private def controlled(action: => Unit) =
    if (aboveDeathThold) action
    else die

  private def tryMutate = {
    import context.dispatcher

    mutateProbability chanceFor {
      Future(mutate).onSuccess { case g => self ! GenUpdate(g) }
    }
  }

  private def talk =
    crossOverProbability chanceFor {
      context.parent ! CrossOverRequest(gen)
    } orElse {
      context.parent ! MeetingRequest(fitness)
    }

  private def die = {
    metricsHub ! DeathRecord
    context stop self
  }

}
