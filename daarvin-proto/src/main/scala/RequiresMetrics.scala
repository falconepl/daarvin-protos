import akka.actor.Props

trait RequiresMetrics {
  def metricsProps: Props
}
