package com.seanshubin.up_to_date.logic

import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

class LauncherTest extends FunSuite with EasyMockSugar {
  test("if configuration if valid, launcher will create a runner and execute the run method") {
    val commandLineArguments = Seq("some configuration file")
    val configurationValidator = mock[ConfigurationValidator]
    val validConfiguration = SampleData.validConfiguration
    val createRunner = mock[ValidConfiguration => Runner]
    val runner = mock[Runner]
    val notifications = mock[Notifications]
    val launcher = new LauncherImpl(commandLineArguments, configurationValidator, createRunner, notifications)

    expecting {
      configurationValidator.validate(commandLineArguments).andReturn(Right(validConfiguration))
      createRunner(validConfiguration).andReturn(runner)
      runner.run()
    }

    whenExecuting(configurationValidator, createRunner, runner) {
      launcher.launch()
    }
  }

  test("if the configuration is not valid, launcher will write an error report to the console") {
    val commandLineArguments = Seq("some configuration file")
    val configurationValidator = mock[ConfigurationValidator]
    val errorReport = Seq("error with configuration")
    val notifications = mock[Notifications]
    val createRunner = mock[ValidConfiguration => Runner]
    val launcher = new LauncherImpl(commandLineArguments, configurationValidator, createRunner, notifications)

    expecting {
      configurationValidator.validate(commandLineArguments).andReturn(Left(errorReport))
      notifications.errorWithConfiguration(Seq("error with configuration"))
    }

    whenExecuting(configurationValidator, notifications) {
      launcher.launch()
    }
  }
}
