package com.seanshubin.up_to_date.logic

import java.nio.file.Path

trait Notifications {
  def errorWithConfiguration(commandLineArguments: Seq[String], errorReport: Seq[String])

  def timeTaken[T](caption: String)(block: => T): T

  def httpGet(uriString: String)

  def httpGetFromCache(uriString: String, path: Path)
}
