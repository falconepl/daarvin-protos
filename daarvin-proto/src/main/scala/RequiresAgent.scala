import akka.actor.{ActorRef, Props}

trait RequiresAgent {
  def agentProps(region: ActorRef, metricsHub: ActorRef, gen: Option[IndexedSeq[Int]] = None): Props
}
