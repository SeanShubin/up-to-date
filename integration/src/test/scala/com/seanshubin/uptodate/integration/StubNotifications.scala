package com.seanshubin.uptodate.integration

import java.net.URI
import java.nio.file.Path

import com.seanshubin.uptodate.logic.{Configuration, Notifications}

import scala.collection.mutable.ArrayBuffer

class StubNotifications extends Notifications {
  val timeTakenCalls = new ArrayBuffer[String]()
  val getCalls = new ArrayBuffer[URI]()

  override def timeTaken[T](caption: String)(block: => T): T = {
    timeTakenCalls.append(caption)
    block
  }

  override def httpGet(uri: URI): Unit = {
    getCalls.append(uri)
  }

  override def errorWithConfiguration(commandLineArguments: Seq[String], errorReport: Seq[String]): Unit = ???

  override def httpGetFromCache(uri: URI, path: Path): Unit = ???

  override def effectiveConfiguration(configuration: Configuration): Unit = ???

  override def uriSyntaxException(uriString: String): Unit = ???
}
