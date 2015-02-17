import akka.actor.FSM

import scala.concurrent.duration._

abstract class Agent
  extends FSM[AgentState, AgentData] with Initialization with Config with Behavior {

  startWith(Waiting, initializeData)

  // Waiting

  when(Waiting, stateTimeout = 1 second) {
    case Event(MeetingRequest(neighborEnergy), data: AgentData) =>
      goto(ProcessMeetingRequest) using data.withNeighbor(sender).copy(
        energy = energyAfterMeeting(data.energy, neighborEnergy)
      )
    case Event(MeetingResponse(neighborEnergy), data: AgentData) =>
      stay using data.copy(
        energy = energyAfterMeeting(data.energy, neighborEnergy)
      )
    case Event(CrossOverRequest(gen), data: AgentData) if aboveThold(data.energy) =>
      goto(LockForCrossOverReceive) using data.withNeighbor(sender)
    case Event(CrossOverResponse(neighborGen), data: AgentData) =>
      goto(ProcessCrossOverApply) using data.withNeighborGen(sender, neighborGen).copy(
        energy = energyAfterCrossOver(data.energy)
      )
    case Event(StateTimeout, _) =>
      stay
  }

  // Meeting

  when(ProcessMeetingRequest) {
    case Event(Done, data: AgentDataWithNeighbor) =>
      goto(Waiting) using data.removeNeighbor
  }

  // Cross-over

  when(LockForCrossOverReceive, stateTimeout = 900 milliseconds) {
    case Event(ReleaseCrossOverLock, data: AgentDataWithNeighbor) =>
      goto(Waiting) using data.removeNeighbor.copy(
        energy = energyAfterCrossOver(data.energy)
      )
    case Event(CrossOverCancel, data: AgentDataWithNeighbor) =>
      goto(Waiting) using data.removeNeighbor
    case Event(StateTimeout, data: AgentDataWithNeighbor) =>
      goto(Waiting) using data.removeNeighbor
  }

  when(ProcessCrossOverApply) {
    case Event(Done, data: AgentDataWithNeighborGen) =>
      goto(Waiting) using data.removeNeighbor
  }

  // Transitions

  onTransition {
    case _ -> ProcessMeetingRequest =>
      nextStateData match {
        case AgentDataWithNeighbor(energy, _, neighbor) =>
          neighbor ! MeetingResponse(energy)
      }
      self ! Done
    case _ -> LockForCrossOverReceive =>
      nextStateData match {
        case AgentDataWithNeighbor(_, gen, neighbor) =>
          neighbor ! CrossOverResponse(gen)
      }
      self ! Done
    case _ -> ProcessCrossOverApply =>
      nextStateData match {
        case AgentDataWithNeighborGen(_, gen, neighbor, neighborGen) =>
          neighbor ! ReleaseCrossOverLock
          val newAgentGen = crossOver(gen, neighborGen)
          val newAgentEnergy = energyForNewAgent
        // TODO: Create new agent/actor
      }
      self ! Done
    case _ -> Waiting =>
    // TODO: Send meeting/cross-over request with certain probability
  }

  // Unhandled

  whenUnhandled {
    case Event(CrossOverResponse, _) =>
      sender ! CrossOverCancel
      stay
  }

}
