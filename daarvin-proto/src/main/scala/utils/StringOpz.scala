package utils

object StringOpz {

  implicit final class StrOpz(private val s: String) extends AnyVal {
    def inlined = s.stripMargin.replace("\n", "")
  }

}
