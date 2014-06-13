package com.seanshubin.up_to_date.logic

class LineEmittingNotifications(emitLine: String => Unit) extends Notifications {
  def errorWithConfiguration(errorReport: Seq[String]) {
    emitLine("Unable to launch application due to configuration validation errors")
    errorReport.foreach(emitLine)
  }
}
