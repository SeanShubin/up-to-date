package com.seanshubin.up_to_date.logic

trait LauncherWiring {
  def commandLineArguments: Seq[String]

  lazy val launcher: Launcher = new LauncherImpl(commandLineArguments, ???, ???, ???)
}
