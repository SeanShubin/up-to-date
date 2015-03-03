package com.seanshubin.up_to_date.integration

import java.nio.file.Path

import com.seanshubin.up_to_date.logic.{Configuration, Notifications}

import scala.collection.mutable.ArrayBuffer

class StubNotifications extends Notifications {
  val timeTakenCalls = new ArrayBuffer[String]()
  val getCalls = new ArrayBuffer[String]()

  override def timeTaken[T](caption: String)(block: => T): T = {
    timeTakenCalls.append(caption)
    block
  }

  override def httpGet(uriString: String): Unit = {
    getCalls.append(uriString)
  }

  override def errorWithConfiguration(commandLineArguments: Seq[String], errorReport: Seq[String]): Unit = ???

  override def httpGetFromCache(uriString: String, path: Path): Unit = ???

  override def effectiveConfiguration(configuration: Configuration): Unit = ???
}
