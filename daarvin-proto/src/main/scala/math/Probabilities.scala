package math

trait Probability extends Any {

  final def chanceFor(action: => Unit) =
    if (success) {
      action
      Succeeded()
    } else Lost()

  protected def success: Boolean

}

object Probabilities {

  private val r = new util.Random

  implicit final class ProbabilityDouble(private val n: Double) extends AnyVal with Probability {
    override def success = r.nextDouble() < n
  }

}
