import akka.actor.ActorRef

import scala.math._

class SimpleAgent(metricsHub: ActorRef, gen: Option[Hm.Gen])
  extends Agent(metricsHub, gen) with SimpleConfig with SimpleAgentBehavior

trait SimpleAgentBehavior extends AgentBehavior with AgentHallmarks {

  import HallmarksImplicits._

  val crossOverCost = 1

  val r = util.Random

  val numbers = (0 to 9).toList

  def crossOverEnergyUpdate = energy - crossOverCost

  def fitnessUpdate = {
    val len = gen.size
    val placement = gen.zipWithIndex.foldLeft(0) {
      case (accum, (elem, idx)) => accum + (len - abs(elem - idx))
    }
    placement * gen.distinct.size
  }

  def aboveEnergyThold = energy > 90

  def aboveDeathThold = energy > 20

  def mutate = {
    val newGen = r.shuffle(gen).tail :+ r.shuffle(numbers).head
    r.shuffle(newGen)
  }

}
