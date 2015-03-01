import scala.concurrent.duration.{FiniteDuration, Duration}

trait Config {

  /**
   * Time for an actor system to operate. After that time a stop
   * message is issued to halt the system and retrieve results.
   */
  def operatingTime: FiniteDuration

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
   * Energy acquired/lost by the agent in a meeting with the other agent.
   */
  def meetingCost: Int

  /**
   * Probability of agent performing a mutation after each message handling.
   */
  def mutateProbability: Double

  /**
   * Probability of agent issuing a cross-over. It is 1 - m where
   * m = probability of agent issuing a meeting with other agent.
   */
  def crossOverProbability: Double

  /**
   * Maximum time a region should wait for the next solution from one
   * of its children.
   */
  def finishTimeout: Duration

}
