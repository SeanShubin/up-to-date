package com.seanshubin.up_to_date.console

object EntryPoint extends App {
  ConfigurationDependencyInjection.createRunner(args).run()
}
