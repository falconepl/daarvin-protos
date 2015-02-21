import akka.actor.Props

trait RequiresAgent {
  def agentProps(gen: Option[IndexedSeq[Int]] = None): Props
}
