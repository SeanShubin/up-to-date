package com.seanshubin.up_to_date.logic

trait LauncherWiring {
  def commandLineArguments: Seq[String]

  def emitLine: String => Unit

  lazy val notifications: Notifications = new LineEmittingNotifications(emitLine)
  lazy val launcher: Launcher = new LauncherImpl(commandLineArguments, ???, ???, notifications)
}
