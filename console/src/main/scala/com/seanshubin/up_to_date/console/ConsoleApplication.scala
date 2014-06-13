package com.seanshubin.up_to_date.console

import com.seanshubin.up_to_date.logic.LauncherWiring

object ConsoleApplication extends App {
  val launcherWiring = new LauncherWiring {
    lazy val commandLineArguments = args.toSeq
  }
  launcherWiring.launcher.launch()
}
