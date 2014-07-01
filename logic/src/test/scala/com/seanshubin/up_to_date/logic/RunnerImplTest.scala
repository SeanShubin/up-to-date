package com.seanshubin.up_to_date.logic

import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

import scala.collection.mutable.ArrayBuffer

class RunnerImplTest extends FunSuite with EasyMockSugar {

  class FakeNotifications extends Notifications {
    val timeTakenCalls = new ArrayBuffer[String]()

    override def errorWithConfiguration(errorReport: Seq[String]): Unit = ???

    override def timeTaken[T](caption: String)(block: => T): T = {
      timeTakenCalls.append(caption)
      block
    }

    override def httpGet(uriString: String): Unit = ???
  }

  test("application flow") {
    val pomFileScanner = mock[PomFileScanner]
    val mavenRepositoryScanner = mock[MavenRepositoryScanner]
    val dependencyUpgradeAnalyzer = mock[DependencyUpgradeAnalyzer]
    val upgrader = mock[Upgrader]
    val reporter = mock[Reporter]
    val notifications = new FakeNotifications
    val runner: Runner = new RunnerImpl(
      pomFileScanner,
      mavenRepositoryScanner,
      dependencyUpgradeAnalyzer,
      upgrader,
      reporter,
      notifications)
    expecting {
      pomFileScanner.scanExistingDependencies().andReturn(SampleData.existingDependencies)
      mavenRepositoryScanner.scanLatestDependencies(SampleData.existingDependencies.toGroupAndArtifactSet).andReturn(SampleData.latestDependencies)
      dependencyUpgradeAnalyzer.outOfDate(SampleData.existingDependencies, SampleData.latestDependencies).andReturn(SampleData.outOfDate)
      upgrader.performAutomaticUpgrades(SampleData.outOfDate).andReturn(SampleData.automaticUpgradesPerformed)
      reporter.reportAutomaticUpgradesPerformed(SampleData.automaticUpgradesPerformed)
      reporter.reportOutOfDate(SampleData.outOfDate)
    }
    whenExecuting(pomFileScanner, mavenRepositoryScanner, dependencyUpgradeAnalyzer, upgrader, reporter) {
      runner.run()
    }
    assert(notifications.timeTakenCalls === Seq("Total Time Taken"))
  }
}
