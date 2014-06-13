package com.seanshubin.up_to_date.logic


class LauncherImpl(commandLineArguments: Seq[String],
                   configurationValidator: ConfigurationValidator,
                   runnerFactory: RunnerFactory,
                   notifications: Notifications) extends Launcher {
  override def launch(): Unit = {
    configurationValidator.validate(commandLineArguments) match {
      case Left(errorReport) =>
        notifications.errorWithConfiguration(errorReport)
      case Right(validConfiguration) =>
        val runner = runnerFactory.create(validConfiguration)
        runner.run()
    }
  }
}
