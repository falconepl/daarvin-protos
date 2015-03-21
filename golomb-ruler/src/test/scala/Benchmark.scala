import org.scalameter.PerformanceTest.Quickbenchmark
import org.scalameter.api._

object Benchmark extends Quickbenchmark {

  import RulerGen._

  val marksRange = Gen.range("marks")(from = 10, upto = 100, hop = 10)

  val rulers = for {
    marks <- marksRange
  } yield genRuler(marks)

  performance of "Ruler violations" in {
    measure method "with loop" in {
      using(rulers) in {
        // Measure ruler violations counting performance HERE
        identity
      }
    }
  }

}
