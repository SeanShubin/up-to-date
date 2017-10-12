package com.seanshubin.uptodate.logic


class Launcher(commandLineArguments: Seq[String],
               configurationValidator: ConfigurationValidator,
               createRunner: Configuration => Runnable,
               notifications: Notifications) extends Runnable {
  override def run(): Unit = {
    configurationValidator.validate(commandLineArguments) match {
      case Left(errorReport) =>
        notifications.errorWithConfiguration(commandLineArguments, errorReport)
      case Right(configuration) =>
        notifications.effectiveConfiguration(configuration)
        val runner = createRunner(configuration)
        runner.run()
    }
  }
}
