package math

sealed trait Draw {
  def orElse(action: => Unit): Unit
}

final case class Succeeded() extends Draw {
  def orElse(action: => Unit): Unit = ()
}

final case class Lost() extends Draw {
  def orElse(action: => Unit): Unit = action
}
