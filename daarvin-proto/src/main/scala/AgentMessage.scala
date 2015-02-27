
sealed trait AgentMessage


case object RegRecipient extends AgentMessage


case class MeetingRequest(fitness: Int) extends AgentMessage

sealed trait EnergyChange extends AgentMessage

case object EnergyGained extends EnergyChange

case object EnergyLost extends EnergyChange

case object MeetingFailed extends AgentMessage


case class CrossOverRequest(gen: IndexedSeq[Int]) extends AgentMessage

case class GenUpdate(gen: IndexedSeq[Int]) extends AgentMessage

case object CrossOverSucceeded extends AgentMessage

case object CrossOverFailed extends AgentMessage


case object Finish extends AgentMessage

case class Solution(gen: IndexedSeq[Int], fitness: Int) extends AgentMessage


case object MetricsRequest extends AgentMessage

case class Metrics(data: MetricsData) extends AgentMessage


sealed trait RecordMessage extends AgentMessage

case object MutateRecord extends RecordMessage

case object MeetingRecord extends RecordMessage

case object CrossOverRecord extends RecordMessage
