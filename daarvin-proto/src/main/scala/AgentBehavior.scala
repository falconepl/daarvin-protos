
trait AgentBehavior { _: Hallmarks =>

  var gen: Gen

  var energy: Energy

  var fitness: Fit

  /**
   * Energy value to set after cross-over action.
   */
  def crossOverEnergyUpdate: Energy

  /**
   * Current fitness value to set.
   */
  def fitnessUpdate: Fit

  /**
   * True if energy level is above a cross-over threshold. False otherwise.
   */
  def aboveEnergyThold: Boolean

  /**
   * True if energy level is above agent's death threshold. False otherwise.
   */
  def aboveDeathThold: Boolean

  /**
   * Genetic data to set after a mutation action.
   */
  def mutate: Gen

}
