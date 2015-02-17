import akka.actor.ActorRef

class AgentData(val energy: Int,
                val gen: IndexedSeq[Int]) {
  def copy(energy: Int = energy, gen: IndexedSeq[Int] = gen) =
    new AgentData(energy, gen)

  def withNeighbor(neighbor: ActorRef) =
    new AgentDataWithNeighbor(energy, gen, neighbor)

  def withNeighborGen(neighbor: ActorRef, neighborGen: IndexedSeq[Int]) =
    new AgentDataWithNeighborGen(energy, gen, neighbor, neighborGen)
}

class AgentDataWithNeighbor(override val energy: Int,
                            override val gen: IndexedSeq[Int],
                            val neighbor: ActorRef) extends AgentData(energy, gen) {
  override def copy(energy: Int = energy, gen: IndexedSeq[Int] = gen) =
    new AgentDataWithNeighbor(energy, gen, neighbor)

  def removeNeighbor =
    new AgentData(energy, gen)
}

object AgentDataWithNeighbor {
  def unapply(a: AgentDataWithNeighbor) = Some((a.energy, a.gen, a.neighbor))
}

class AgentDataWithNeighborGen(override val energy: Int,
                               override val gen: IndexedSeq[Int],
                               val neighbor: ActorRef,
                               val neighborGen: IndexedSeq[Int]) extends AgentData(energy, gen) {
  override def copy(energy: Int = energy, gen: IndexedSeq[Int] = gen) =
    new AgentDataWithNeighborGen(energy, gen, neighbor, neighborGen)

  def removeNeighbor =
    new AgentData(energy, gen)
}

object AgentDataWithNeighborGen {
  def unapply(a: AgentDataWithNeighborGen) = Some((a.energy, a.gen, a.neighbor, a.neighborGen))
}
