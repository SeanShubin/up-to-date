package com.seanshubin.up_to_date.logic

import java.nio.file.Paths

import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

class LauncherTest extends FunSuite with EasyMockSugar {
  private val sample = new Configuration(
    pomFileName = "pom.xml",
    directoryNamesToSkip = Set("target"),
    directoriesToSearch = Seq(Paths.get(".")),
    mavenRepositories = Seq(
      "http://repo.maven.apache.org/maven2",
      "http://onejar-maven-plugin.googlecode.com/svn/mavenrepo",
      "http://oss.sonatype.org/content/groups/scala-tools"),
    doNotUpgradeFrom = Set(GroupAndArtifact("groupIdToIgnore", "artifactIdToIgnore")),
    doNotUpgradeTo = Set(GroupArtifactVersion("groupIdToIgnore", "artifactIdToIgnore", "1.2.3")),
    automaticallyUpgrade = true,
    reportDirectory = Paths.get("generated", "sample", "report"),
    cacheDirectory = Paths.get("generated", "cache"),
    cacheExpire = "5 days",
    substitutions = Map(
      "${scala.major}" -> "2.11",
      "${scala.major.minor}" -> "2.11.6")
  )

  test("if configuration if valid, launcher will create a runner and execute the run method") {
    val commandLineArguments = Seq("some-configuration-file")
    val configurationValidator = mock[ConfigurationValidator]
    val createRunner = mock[Configuration => Runner]
    val runner = mock[Runner]
    val notifications = mock[Notifications]
    val launcher = new LauncherImpl(commandLineArguments, configurationValidator, createRunner, notifications)

    expecting {
      configurationValidator.validate(commandLineArguments).andReturn(Right(sample))
      createRunner(sample).andReturn(runner)
      runner.run()
    }

    whenExecuting(configurationValidator, createRunner, runner) {
      launcher.launch()
    }
  }

  test("if the configuration is not valid, launcher will write an error report to the console") {
    val commandLineArguments = Seq("some-configuration-file")
    val configurationValidator = mock[ConfigurationValidator]
    val errorReport = Seq("error with configuration")
    val notifications = mock[Notifications]
    val createRunner = mock[Configuration => Runner]
    val launcher = new LauncherImpl(commandLineArguments, configurationValidator, createRunner, notifications)

    expecting {
      configurationValidator.validate(commandLineArguments).andReturn(Left(errorReport))
      notifications.errorWithConfiguration(commandLineArguments, Seq("error with configuration"))
    }

    whenExecuting(configurationValidator, notifications) {
      launcher.launch()
    }
  }
}
