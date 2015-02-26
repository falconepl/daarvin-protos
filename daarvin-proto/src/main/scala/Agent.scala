import akka.actor.{Actor, ActorRef}
import math.Probabilities._

import scala.concurrent.Future

abstract class Agent(region: ActorRef, specificGen: Option[IndexedSeq[Int]] = None)
  extends Actor with Config with AgentBehavior {

  var energy = initEnergy

  var gen = specificGen getOrElse initGen

  var fitness = fitnessUpdate

  override def preStart() = controlled { talk }

  def receive = {
    case EnergyGained =>
      energy += meetingCost
      controlled { tryMutate orElse talk }

    case EnergyLost =>
      energy -= meetingCost
      controlled { tryMutate orElse talk }

    case MeetingFailed =>
      controlled { tryMutate orElse talk }

    case GenUpdate(update) =>
      gen = update
      fitness = fitnessUpdate
      controlled { talk }

    case CrossOverSucceeded =>
      energy = crossOverEnergyUpdate
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
      context.parent ! CrossOverRequest(gen)
    } orElse {
      context.parent ! MeetingRequest(fitness)
    }

}
