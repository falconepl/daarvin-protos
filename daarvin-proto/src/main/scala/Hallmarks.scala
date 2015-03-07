
trait Hallmarks {
  type Gen
  type Energy <: Changeable[Energy]
  type Fit <: Ordered[Fit]
}


trait Changeable[T] extends Any {
  def +(value: T): T

  def -(value: T): T
}
