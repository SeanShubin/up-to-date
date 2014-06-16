package com.seanshubin.up_to_date.logic

class LineEmittingNotifications(emitLine: String => Unit) extends Notifications {
  override def errorWithConfiguration(errorReport: Seq[String]): Unit = {
    emitLine("Unable to launch application due to configuration validation errors")
    errorReport.foreach(emitLine)
  }

  override def timeTaken(startTime: Long, endTime: Long): Unit = {
    val duration = endTime - startTime
    val durationString = DurationFormat.MillisecondsFormat.format(duration)
    emitLine(s"Total time taken: $durationString")
  }
}
