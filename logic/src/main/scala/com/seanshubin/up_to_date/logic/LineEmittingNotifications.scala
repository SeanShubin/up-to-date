package com.seanshubin.up_to_date.logic

class LineEmittingNotifications(systemClock: SystemClock, emitLine: String => Unit) extends Notifications {
  override def errorWithConfiguration(errorReport: Seq[String]): Unit = {
    emitLine("Unable to launch application due to configuration validation errors")
    errorReport.foreach(emitLine)
  }

  override def timeTaken[T](caption: String)(block: => T): T = {
    val startTime = systemClock.currentTimeMillis
    val result = block
    val endTime = systemClock.currentTimeMillis
    val duration = endTime - startTime
    val durationString = DurationFormat.MillisecondsFormat.format(duration)
    emitLine(s"$caption: $durationString")
    result
  }

  override def httpGet(uriString: String): Unit = {
    emitLine(s"GET: $uriString")
  }
}
