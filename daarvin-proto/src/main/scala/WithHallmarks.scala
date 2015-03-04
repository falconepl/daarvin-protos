
trait AgentHallmarks extends WithGen with WithEnergy with WithFit


trait WithGen {
  type Gen
}

trait WithEnergy {
  type Energy <: Expandable[Energy] with Contractive[Energy]
}

trait WithFit {
  type Fit <: Ordered[Fit]
}


trait Expandable[-T] {
  def +=(value: T): this.type
}

trait Contractive[-T] {
  def -=(value: T): this.type
}
