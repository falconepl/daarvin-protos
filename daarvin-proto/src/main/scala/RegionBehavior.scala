
trait RegionBehavior {

  def meetingEnergyUpdate(selfEnergy: Int, neighborEnergy: Int): Int

  def crossOver(genLeft: IndexedSeq[Int], genRight: IndexedSeq[Int]): IndexedSeq[Int]

}
