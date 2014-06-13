package com.seanshubin.up_to_date.logic

import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

class LauncherTest extends FunSuite with EasyMockSugar {
  test("if configuration if valid, launcher will create a runner and execute the run method") {
    val commandLineArguments = Seq("some configuration file")
    val configurationValidator = mock[ConfigurationValidator]
    val validConfiguration = SampleData.validConfiguration
    val runnerFactory = mock[RunnerFactory]
    val runner = mock[Runner]
    val notifications = mock[Notifications]
    val launcher = new LauncherImpl(commandLineArguments, configurationValidator, runnerFactory, notifications)

    expecting {
      configurationValidator.validate(commandLineArguments).andReturn(Right(validConfiguration))
      runnerFactory.create(validConfiguration).andReturn(runner)
      runner.run()
    }

    whenExecuting(configurationValidator, runnerFactory, runner) {
      launcher.launch()
    }
  }

  test("if the configuration is not valid, launcher will write an error report to the console") {
    val commandLineArguments = Seq("some configuration file")
    val configurationValidator = mock[ConfigurationValidator]
    val errorReport = Seq("error with configuration")
    val notifications = mock[Notifications]
    val runnerFactory = mock[RunnerFactory]
    val launcher = new LauncherImpl(commandLineArguments, configurationValidator, runnerFactory, notifications)

    expecting {
      configurationValidator.validate(commandLineArguments).andReturn(Left(errorReport))
      notifications.errorWithConfiguration(Seq("error with configuration"))
    }

    whenExecuting(configurationValidator, notifications) {
      launcher.launch()
    }
  }
}
