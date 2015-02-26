import akka.actor.Props

trait RequiresRegion {
  def regionProps: Props
}
