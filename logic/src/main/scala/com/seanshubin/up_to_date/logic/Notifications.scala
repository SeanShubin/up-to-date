package com.seanshubin.up_to_date.logic

import java.net.URI
import java.nio.file.Path

trait Notifications {
  def uriSyntaxException(uriString: String)

  def effectiveConfiguration(configuration: Configuration)

  def errorWithConfiguration(commandLineArguments: Seq[String], errorReport: Seq[String])

  def timeTaken[T](caption: String)(block: => T): T

  def httpGet(uri: URI)

  def httpGetFromCache(uri: URI, path: Path)
}
