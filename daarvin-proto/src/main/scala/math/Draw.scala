package math

sealed trait Draw {
  def orElse(action: => Unit): Unit
}


final class Succeeded() extends Draw {
  def orElse(action: => Unit): Unit = action
}

object Succeeded {
  def apply() = new Succeeded()
}


final class Lost() extends Draw {
  def orElse(action: => Unit): Unit = ()
}

object Lost {
  def apply() = new Lost()
}
