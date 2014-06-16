package com.seanshubin.up_to_date.console

import com.seanshubin.up_to_date.integration.FileSystemImpl
import com.seanshubin.up_to_date.logic.{FileSystem, LauncherWiring}

object ConsoleApplication extends App {
  val launcherWiring = new LauncherWiring {
    lazy val commandLineArguments: Seq[String] = args
    lazy val emitLine: String => Unit = println
    lazy val fileSystem: FileSystem = new FileSystemImpl(charsetName)
  }
  launcherWiring.launcher.launch()
}
