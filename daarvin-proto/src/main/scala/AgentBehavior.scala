
trait AgentBehavior {

  var gen: IndexedSeq[Int]

  var energy: Int

  /**
   * Energy value to set after cross-over action.
   */
  def crossOverEnergyUpdate: Int

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
  def mutate: IndexedSeq[Int]

}
