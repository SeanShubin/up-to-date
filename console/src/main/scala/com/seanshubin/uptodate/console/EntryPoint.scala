package com.seanshubin.uptodate.console

object EntryPoint extends App {
  CommandLineDependencyInjection.createRunner(args).run()
}
