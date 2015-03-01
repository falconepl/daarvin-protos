import akka.actor.{Actor, ActorLogging}
import utils.LazyLog

class MetricsHub extends Actor with ActorLogging with LazyLog {

  var mutations: BigDecimal = 0
  var meetings: BigDecimal = 0
  var crossOvers: BigDecimal = 0
  var deaths: BigDecimal = 0

  llog.info("Starting metrics hub")

  def receive = {
    case MutateRecord =>
      mutations = mutations + 1
    case MeetingRecord =>
      meetings = meetings + 1
    case CrossOverRecord =>
      crossOvers = crossOvers + 1
    case DeathRecord =>
      deaths = deaths + 1
    case MetricsRequest =>
      llog.debug("Sending metrics data")
      sender ! AgentsMetrics(mutations, meetings, crossOvers, deaths)
      context stop self
  }

}
