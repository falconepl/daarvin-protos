import akka.actor.{Actor, ActorRef}
import math.Probabilities._

import scala.concurrent.Future

abstract class Agent(region: ActorRef, specificGen: Option[IndexedSeq[Int]] = None)
  extends Actor with Config with AgentBehavior {

  var energy = initEnergy

  var gen = specificGen getOrElse initGen

  override def preStart() = controlled { talk }

  def receive = {
    case EnergyUpdate(update) =>
      energy = update
      controlled { tryMutate orElse talk }

    case GenUpdate(update) =>
      gen = update
      controlled { talk }

    case CrossOverSucceeded =>
      energy = crossOverEnergyUpdate
      controlled { tryMutate orElse talk }

    case EnergyUpdateFailed =>
      controlled { tryMutate orElse talk }

    case CrossOverFailed =>
      controlled { tryMutate orElse talk }
  }

  private def controlled(action: => Unit) =
    if (aboveDeathThold) action
    else context stop self

  private def tryMutate = {
    import context.dispatcher

    mutateProbability chanceFor {
      Future(mutate).onSuccess { case g => self ! GenUpdate(g) }
    }
  }

  private def talk =
    crossOverProbability chanceFor {
      context.parent ! CrossOverRequest
    } orElse {
      context.parent ! MeetingRequest
    }

}
