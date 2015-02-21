import akka.actor.ActorRef

sealed abstract class AgentInfo(val ref: ActorRef)

case class MeetingInfo(override val ref: ActorRef, energy: Int) extends AgentInfo(ref)

case class CrossOverInfo(override val ref: ActorRef, gen: IndexedSeq[Int]) extends AgentInfo(ref)
