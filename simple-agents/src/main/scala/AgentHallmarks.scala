import scala.language.implicitConversions
import scala.math.Ordered

/**
 * Agent's hallmarks.
 */
object Hm {
  type Gen = IndexedSeq[Int]
  type Energy = AgentEnergy
  type Fit = AgentFitness
}

/**
 * Helper trait to mix-in `Hm` types.
 */
trait AgentHallmarks extends Hallmarks {
  override type Gen = Hm.Gen
  override type Energy = Hm.Energy
  override type Fit = Hm.Fit
}

case class AgentEnergy(n: Int) extends AnyVal with Changeable[AgentEnergy] {
  override def -(value: AgentEnergy) = AgentEnergy(n - value.n)

  override def +(value: AgentEnergy) = AgentEnergy(n + value.n)
}

case class AgentFitness(n: Int) extends AnyVal with Ordered[AgentFitness] {
  override def compare(that: AgentFitness) = n compare that.n
}

/**
 * Implicit conversions.
 */
object HallmarksImplicits {
  implicit def agentEnergyInt(en: AgentEnergy): Int = en.n

  implicit def intAgentEnergy(n: Int): AgentEnergy = AgentEnergy(n)


  implicit def agentFitnessInt(fit: AgentFitness): Int = fit.n

  implicit def intAgentFitness(n: Int): AgentFitness = AgentFitness(n)
}
