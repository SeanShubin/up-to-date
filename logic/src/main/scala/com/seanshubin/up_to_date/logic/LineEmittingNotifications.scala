package com.seanshubin.up_to_date.logic

class LineEmittingNotifications(emitLine: String => Unit) extends Notifications {
  override def errorWithConfiguration(errorReport: Seq[String]): Unit = {
    emitLine("Unable to launch application due to configuration validation errors")
    errorReport.foreach(emitLine)
  }
}
