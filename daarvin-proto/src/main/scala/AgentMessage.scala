
sealed trait AgentMessage


case class MeetingRequest(fitness: Int) extends AgentMessage

sealed trait EnergyChange extends AgentMessage

case object EnergyGained extends EnergyChange

case object EnergyLost extends EnergyChange

case object MeetingFailed extends AgentMessage


case class CrossOverRequest(gen: IndexedSeq[Int]) extends AgentMessage

case class GenUpdate(gen: IndexedSeq[Int]) extends AgentMessage

case object CrossOverSucceeded extends AgentMessage

case object CrossOverFailed extends AgentMessage
