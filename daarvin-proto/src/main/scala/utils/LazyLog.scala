package utils

import akka.actor.ActorLogging

/**
 * Using lazy logging prevents message string evaluation if log level for a given
 * logger call is not set. In turn it provides performance optimization.
 */
trait LazyLog { _: ActorLogging =>

  object llog {

    @inline def debug(message: => String) =
      if (log.isDebugEnabled) log.debug(message)

    @inline def error(message: => String) =
      if (log.isErrorEnabled) log.error(message)

    @inline def info(message: => String) =
      if (log.isInfoEnabled) log.info(message)

    @inline def warning(message: => String) =
      if (log.isWarningEnabled) log.warning(message)

  }

}
