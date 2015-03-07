import akka.actor.{ActorRef, Props}

trait RequiresAgent { _: Hallmarks =>
  def agentProps(metricsHub: ActorRef, gen: Option[Gen] = None): Props
}
