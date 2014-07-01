package com.seanshubin.up_to_date.logic

trait Notifications {
  def errorWithConfiguration(errorReport: Seq[String])

  def timeTaken[T](caption: String)(block: => T): T

  def httpGet(uriString: String)
}
