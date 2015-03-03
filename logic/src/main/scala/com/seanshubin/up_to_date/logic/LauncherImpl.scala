package com.seanshubin.up_to_date.logic


class LauncherImpl(commandLineArguments: Seq[String],
                   configurationValidator: ConfigurationValidator,
                   createRunner: Configuration => Runner,
                   notifications: Notifications) extends Launcher {
  override def launch(): Unit = {
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
