package com.seanshubin.up_to_date.console

import java.nio.charset.Charset

import com.seanshubin.devon.core.devon.{DefaultDevonMarshaller, DevonMarshaller}
import com.seanshubin.up_to_date.integration.{FileSystemImpl, SystemClockImpl}
import com.seanshubin.up_to_date.logic._

trait LauncherWiring {
  def commandLineArguments: Seq[String]

  lazy val createRunner: Configuration => Runner =
    validConfiguration => RunnerWiring(validConfiguration).runner
  lazy val charsetName: String = "utf-8"
  lazy val charset: Charset = Charset.forName(charsetName)
  lazy val emitLine: String => Unit = println
  lazy val fileSystem: FileSystem = new FileSystemImpl(charset)
  lazy val systemClock: SystemClock = new SystemClockImpl
  lazy val devonMarshaller: DevonMarshaller = DefaultDevonMarshaller
  lazy val notifications: Notifications = new LineEmittingNotifications(systemClock, devonMarshaller, emitLine)
  lazy val configurationValidator: ConfigurationValidator = new ConfigurationValidatorImpl(
    fileSystem, devonMarshaller)
  lazy val launcher: Launcher = new LauncherImpl(
    commandLineArguments, configurationValidator, createRunner, notifications)
}

object LauncherWiring {
  def apply(theCommandLineArguments: Seq[String]) = new LauncherWiring {
    override def commandLineArguments: Seq[String] = theCommandLineArguments
  }
}
