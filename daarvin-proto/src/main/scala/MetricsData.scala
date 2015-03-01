import utils.StringOpz._

sealed trait MetricsData

case class AgentsMetrics(mutations: BigDecimal = 0,
                         meetings: BigDecimal = 0,
                         crossOvers: BigDecimal = 0,
                         deaths: BigDecimal = 0) extends MetricsData {
  override def toString =
    s"""AgentsMetrics
       |(mutations = $mutations, meetings = $meetings,
       | cross-overs = $crossOvers, deaths = $deaths)""".inlined

}

case class NoMetricsData(reason: String) extends MetricsData
