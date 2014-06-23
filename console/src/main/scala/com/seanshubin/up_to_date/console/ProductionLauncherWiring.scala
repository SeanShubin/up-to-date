package com.seanshubin.up_to_date.console

import com.seanshubin.up_to_date.integration.FileSystemImpl
import com.seanshubin.up_to_date.logic._

trait ProductionLauncherWiring {
  def commandLineArguments: Seq[String]

  lazy val createRunner: ValidConfiguration => Runner =
    validConfiguration => ProductionRunnerWiring(validConfiguration).runner
  lazy val charsetName: String = "UTF-8"
  lazy val emitLine: String => Unit = println
  lazy val fileSystem: FileSystem = new FileSystemImpl(charsetName)
  lazy val notifications: Notifications = new LineEmittingNotifications(emitLine)
  lazy val jsonMarshaller: JsonMarshaller = new JsonMarshallerImpl()
  lazy val configurationValidator: ConfigurationValidator = new ConfigurationValidatorImpl(fileSystem, jsonMarshaller)
  lazy val launcher: Launcher = new LauncherImpl(commandLineArguments, configurationValidator, createRunner, notifications)
}

object ProductionLauncherWiring {
  def apply(theCommandLineArguments: Seq[String]) = new ProductionLauncherWiring {
    override def commandLineArguments: Seq[String] = theCommandLineArguments
  }
}
