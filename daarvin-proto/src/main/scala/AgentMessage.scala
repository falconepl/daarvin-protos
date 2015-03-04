
sealed trait AgentMessage


case object RegRecipient extends AgentMessage


case class MeetingRequest[F <: Ordered[F]](fitness: F) extends AgentMessage

sealed trait EnergyChange extends AgentMessage

case object EnergyGained extends EnergyChange

case object EnergyLost extends EnergyChange

case object MeetingFailed extends AgentMessage


case class CrossOverRequest[G](gen: G) extends AgentMessage

case class GenUpdate[G](gen: G) extends AgentMessage

case object CrossOverSucceeded extends AgentMessage

case object CrossOverFailed extends AgentMessage


case object Finish extends AgentMessage

case class Solution[G, F <: Ordered[F]](gen: G, fitness: F) extends AgentMessage


case object MetricsRequest extends AgentMessage

case class Metrics(data: MetricsData) extends AgentMessage


sealed trait RecordMessage extends AgentMessage

case object MutateRecord extends RecordMessage

case object MeetingRecord extends RecordMessage

case object CrossOverRecord extends RecordMessage

case object DeathRecord extends RecordMessage
