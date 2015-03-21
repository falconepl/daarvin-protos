import scala.annotation.tailrec

/**
 * Golomb ruler generator.
 */
object RulerGen {

  /** Violation count **/
  case class ViolCount(contrib: Double, repet: Int) {
    def toGaps = ((1 / (1 * contrib)).toInt, repet)
  }

  val markHops = circle (Seq(2, 11, 3, 12, 1, 7))
  val violations = Seq(ViolCount(0.2, 3), ViolCount(0.05, 7), ViolCount(0.01, 15))
  val violHops = circle (Seq(3, 5, 2))

  def circle[A](xs: Seq[A]) = Iterator.continually(xs).flatten

  def withEach(prob: Double) = (1 / (1 * prob)).toInt

  def genRuler(marks: Int) = {
    def optViol(ruler: Seq[Int]): Seq[Int] = {
      val repet = violations
        .sortBy(_.contrib)
        .map(_.toGaps)
        .find{ case (gap, rep) => ruler.distinct.length % gap == 0 }
        .map{ case (_, rep) => rep }

      repet.fold(ruler){
        rep => ruler ++ ruler.lastOption.fold(ruler){ Seq.fill(rep - 1)(_) }
      }
    }

    @tailrec def loop(ruler: Seq[Int], nextMark: Int): Seq[Int] = {
      if (ruler.length >= marks) ruler
      else loop(optViol(ruler) :+ nextMark, nextMark + markHops.next())
    }

    loop(Seq.empty[Int], markHops.next())
  }

}
