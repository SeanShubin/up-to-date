package com.seanshubin.up_to_date.logic

import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

class RunnerImplTest extends FunSuite with EasyMockSugar {
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
