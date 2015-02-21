
trait Config {

  /**
   * Number of agents for a single region.
   */
  def regionAgents: Int

  /**
   * Agent's initial energy level.
   */
  def initEnergy: Int

  /**
   * Agent's initial genetic data.
   */
  def initGen: IndexedSeq[Int]

  /**
   * Probability of agent performing a mutation after each message handling.
   */
  def mutateProbability: Double

  /**
   * Probability of agent issuing a cross-over. It is 1 - m where
   * m = probability of agent issuing a meeting with other agent.
   */
  def crossOverProbability: Double

}
