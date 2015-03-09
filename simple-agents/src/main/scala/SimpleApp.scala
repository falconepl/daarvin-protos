import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration._
import scala.language.postfixOps

object SimpleApp extends App with AgentHallmarks {

  import SimpleApp.system.dispatcher

  implicit val timeout = Timeout(5 minutes)

  val system = ActorSystem("simple-agents-sys")

  val future = (system.actorOf(SimpleRegion.regionProps, "region") ? RegRecipient)
    .mapTo[((Gen, Fit), MetricsData)]

  future.onSuccess {
    case ((gen, fitness), metrics) =>
      println(
        s"""[SUCCESS]
           | gen data: $gen
           | fitness: $fitness
           | metrics: $metrics
           |""".stripMargin
      )

      system.shutdown()
  }

  future.onFailure {
    case e =>
      println(
        s"""[FAILURE]
           | $e
           |""".stripMargin
      )

      system.shutdown()
  }

}
