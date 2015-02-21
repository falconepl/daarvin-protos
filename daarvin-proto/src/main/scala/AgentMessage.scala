
sealed trait AgentMessage

case class MeetingRequest(energy: Int) extends AgentMessage

case class CrossOverRequest(gen: IndexedSeq[Int]) extends AgentMessage

case class EnergyUpdate(delta: Int) extends AgentMessage

case class GenUpdate(gen: IndexedSeq[Int]) extends AgentMessage

case object EnergyUpdateFailed extends AgentMessage

case object CrossOverSucceeded extends AgentMessage

case object CrossOverFailed extends AgentMessage
