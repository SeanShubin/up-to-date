package com.seanshubin.up_to_date.logic

trait LauncherWiring {
  def commandLineArguments: Seq[String]

  def emitLine: String => Unit

  def fileSystem: FileSystem

  lazy val charsetName: String = "UTF-8"
  lazy val notifications: Notifications = new LineEmittingNotifications(emitLine)
  lazy val jsonMarshaller: JsonMarshaller = new JsonMarshallerImpl()
  lazy val configurationValidator: ConfigurationValidator = new ConfigurationValidatorImpl(fileSystem, jsonMarshaller)
  lazy val launcher: Launcher = new LauncherImpl(commandLineArguments, configurationValidator, ???, notifications)
}
