
trait Behavior {

  def energyAfterMeeting(selfEnergy: Int, neighborEnergy: Int): Int

  def energyAfterCrossOver(energy: Int): Int

  def aboveThold(energy: Int): Boolean

  def mutate(gen: IndexedSeq[Int]): IndexedSeq[Int]

  def crossOver(genLeft: IndexedSeq[Int], genRight: IndexedSeq[Int]): IndexedSeq[Int]

}
