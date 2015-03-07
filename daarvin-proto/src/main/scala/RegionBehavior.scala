
trait RegionBehavior { _: Hallmarks =>

  def hasBetterFitness(selfFitness: Fit, neighborFitness: Fit): Boolean

  def crossOver(genLeft: Gen, genRight: Gen): Gen

}
