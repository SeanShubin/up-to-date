package com.seanshubin.up_to_date.logic

import java.net.URI
import java.nio.file.Path

import scala.collection.mutable.ArrayBuffer

class StubNotifications extends Notifications {
  val timeTakenCalls = new ArrayBuffer[String]()

  override def timeTaken[T](caption: String)(block: => T): T = {
    timeTakenCalls.append(caption)
    block
  }

  override def errorWithConfiguration(commandLineArguments: Seq[String], errorReport: Seq[String]): Unit = ???

  override def httpGet(uri: URI): Unit = ???

  override def httpGetFromCache(uri: URI, path: Path): Unit = ???

  override def effectiveConfiguration(configuration: Configuration): Unit = ???

  override def uriSyntaxException(uriString: String): Unit = ???
}
