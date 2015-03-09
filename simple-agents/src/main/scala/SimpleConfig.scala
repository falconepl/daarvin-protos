
trait SimpleConfig extends Config with AgentHallmarks {

  import HallmarksImplicits._

  import scala.concurrent.duration._
  import scala.language.postfixOps

  def operatingTime = 3 seconds

  def regionAgents = 100

  def initEnergy = 50

  def initGen = IndexedSeq.fill(10)(0)

  def meetingCost = 5

  def mutateProbability = 0.6

  def crossOverProbability = 0.5

  def finishTimeout = 1 second

}
