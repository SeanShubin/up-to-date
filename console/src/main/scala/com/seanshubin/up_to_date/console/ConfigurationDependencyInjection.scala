package com.seanshubin.up_to_date.console

import java.nio.charset.Charset

import com.seanshubin.devon.domain.{DevonMarshaller, DevonMarshallerWiring}
import com.seanshubin.up_to_date.integration.{FileSystemImpl, SystemClockImpl}
import com.seanshubin.up_to_date.logic._

trait ConfigurationDependencyInjection {
  def commandLineArguments: Seq[String]

  lazy val createRunner: Configuration => Runnable = ApplicationDependencyInjection.createRunner
  lazy val charsetName: String = "utf-8"
  lazy val charset: Charset = Charset.forName(charsetName)
  lazy val emitLine: String => Unit = println
  lazy val fileSystem: FileSystem = new FileSystemImpl(charset)
  lazy val systemClock: SystemClock = new SystemClockImpl
  lazy val devonMarshaller: DevonMarshaller = DevonMarshallerWiring.Default
  lazy val notifications: Notifications = new LineEmittingNotifications(systemClock, devonMarshaller, emitLine)
  lazy val configurationValidator: ConfigurationValidator = new ConfigurationValidatorImpl(
    fileSystem, devonMarshaller)
  lazy val launcher: Runnable = new LauncherImpl(
    commandLineArguments, configurationValidator, createRunner, notifications)
}

object ConfigurationDependencyInjection {
  def createRunner: Seq[String] => Runnable = theCommandLineArguments => new ConfigurationDependencyInjection {
    override def commandLineArguments: Seq[String] = theCommandLineArguments
  }.launcher
}
