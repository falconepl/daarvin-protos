
trait RegionBehavior { _: Config with WithGen with WithFit =>

  def hasBetterFitness(selfFitness: Fit, neighborFitness: Fit): Boolean

  def crossOver(genLeft: Gen, genRight: Gen): Gen

}
