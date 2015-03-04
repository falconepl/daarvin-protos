import akka.actor.{ActorRef, Props}

trait RequiresAgent[G] {
  def agentProps(metricsHub: ActorRef, gen: Option[G] = None): Props
}
