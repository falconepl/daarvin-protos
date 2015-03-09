import akka.actor.{ActorRef, Props}

class SimpleRegion
  extends Region[Hm.Gen, Hm.Energy, Hm.Fit] with SimpleConfig with SimpleRegionBehavior {

  import HallmarksImplicits._

  override val genLen = initGen.length

  override var bestSolution: (Gen, Fit) = (IndexedSeq.empty[Int], 0)

  override def agentProps(metricsHub: ActorRef, gen: Option[Gen] = None) =
    Props(new SimpleAgent(metricsHub, gen))

  override def metricsProps =
    Props(new MetricsHub)

}

object SimpleRegion extends RequiresRegion {
  override def regionProps = Props(new SimpleRegion)
}

trait SimpleRegionBehavior extends RegionBehavior with AgentHallmarks {

  val genLen: Int

  def hasBetterFitness(selfFitness: Fit, neighborFitness: Fit) =
    selfFitness > neighborFitness

  def crossOver(genLeft: Gen, genRight: Gen) = {
    val (leftLen, rightLen) = (genLen / 2 + genLen % 2, genLen / 2)
    genLeft.take(leftLen) ++ genRight.takeRight(rightLen)
  }

}
