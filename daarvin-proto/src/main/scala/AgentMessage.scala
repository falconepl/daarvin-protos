
sealed trait AgentMessage

// Meeting

sealed trait MeetingMessage extends AgentMessage

case class MeetingRequest(energy: Int) extends MeetingMessage

case class MeetingResponse(energy: Int) extends MeetingMessage

// Cross-over

sealed trait CrossOverMessage extends AgentMessage

case class CrossOverRequest(gen: IndexedSeq[Int]) extends CrossOverMessage

case class CrossOverResponse(gen: IndexedSeq[Int]) extends CrossOverMessage

case object ReleaseCrossOverLock extends CrossOverMessage

case object CrossOverCancel extends CrossOverMessage

// Misc.

case object Done extends AgentMessage
