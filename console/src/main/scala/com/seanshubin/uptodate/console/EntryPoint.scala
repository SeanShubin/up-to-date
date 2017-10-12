package com.seanshubin.uptodate.console

object EntryPoint extends App {
  ConfigurationDependencyInjection.createRunner(args).run()
}
