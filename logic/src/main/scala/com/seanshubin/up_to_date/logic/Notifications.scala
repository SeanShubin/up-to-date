package com.seanshubin.up_to_date.logic

trait Notifications {
  def errorWithConfiguration(errorReport: Seq[String])

  def timeTaken(startTime: Long, endTime: Long)
}
