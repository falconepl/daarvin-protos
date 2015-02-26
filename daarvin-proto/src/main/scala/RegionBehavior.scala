
trait RegionBehavior extends Config {

  def hasBetterFitness(selfFitness: Int, neighborFitness: Int): Boolean

  def crossOver(genLeft: IndexedSeq[Int], genRight: IndexedSeq[Int]): IndexedSeq[Int]

}
