
sealed trait AgentState

case object Waiting extends AgentState

case object ProcessMeetingRequest extends AgentState

case object LockForCrossOverReceive extends AgentState

case object ProcessCrossOverApply extends AgentState
