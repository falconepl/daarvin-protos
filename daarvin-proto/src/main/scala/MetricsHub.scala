import akka.actor.Actor

class MetricsHub extends Actor {

  var mutations: BigDecimal = 0
  var meetings: BigDecimal = 0
  var crossOvers: BigDecimal = 0

  def receive = {
    case MutateRecord =>
      mutations = mutations + 1
    case MeetingRecord =>
      meetings = meetings + 1
    case CrossOverRecord =>
      crossOvers = crossOvers + 1
    case MetricsRequest =>
      sender ! AgentsMetrics(mutations, meetings, crossOvers)
      context stop self
  }

}
