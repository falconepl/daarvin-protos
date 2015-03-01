import akka.actor.{ActorRef, Props}

trait RequiresAgent {
  def agentProps(metricsHub: ActorRef, gen: Option[IndexedSeq[Int]] = None): Props
}
