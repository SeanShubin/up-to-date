package com.seanshubin.up_to_date.logic

import java.nio.file.Path

import com.seanshubin.devon.core.devon.DevonMarshaller

class LineEmittingNotifications(systemClock: SystemClock, devonMarshaller: DevonMarshaller, emitLine: String => Unit) extends Notifications {
  override def effectiveConfiguration(configuration: Configuration): Unit = {
    val devon = devonMarshaller.fromValue(configuration)
    val pretty = devonMarshaller.toPretty(devon)
    emitLine("Effective configuration:")
    pretty.foreach(emitLine)

  }

  override def errorWithConfiguration(commandLineArguments: Seq[String], errorReport: Seq[String]): Unit = {
    emitLine(commandLineArguments.mkString(" "))
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

  override def httpGetFromCache(uriString: String, path: Path): Unit = {
    httpGet(uriString)
    emitLine(s"GET(cache): $path")
  }
}
