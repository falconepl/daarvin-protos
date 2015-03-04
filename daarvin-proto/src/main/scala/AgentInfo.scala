import akka.actor.ActorRef

sealed abstract class AgentInfo(val ref: ActorRef)

case class MeetingInfo[F <: Ordered[F]](override val ref: ActorRef, fitness: F) extends AgentInfo(ref)

case class CrossOverInfo[G](override val ref: ActorRef, gen: G) extends AgentInfo(ref)
