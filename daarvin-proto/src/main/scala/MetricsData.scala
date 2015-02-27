
sealed trait MetricsData

case class AgentsMetrics(mutations: BigDecimal = 0,
                         meetings: BigDecimal = 0,
                         crossOvers: BigDecimal = 0) extends MetricsData

case class NoMetricsData(reason: String) extends MetricsData
