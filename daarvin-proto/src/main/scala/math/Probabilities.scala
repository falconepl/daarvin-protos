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

  import scala.language.implicitConversions

  private val r = util.Random

  implicit final class ProbabilityDouble(private val n: Double) extends AnyVal with Probability {
    override def success = r.nextDouble() < n
  }

  implicit def ProbabilityToBoolean(d: Draw): Boolean =
    d match {
      case Succeeded() => true
      case Lost() => false
    }

  def drawnWithChance(p: Probability) = p chanceFor {}

}
