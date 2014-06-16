package com.seanshubin.up_to_date.logic

import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

class RunnerImplTest extends FunSuite with EasyMockSugar {
  test("application flow") {
    val arbitraryStartTime = 12345L
    val arbitraryEndTime = 67890L
    val systemClock: SystemClock = mock[SystemClock]
    val pomFileScanner = mock[PomFileScanner]
    val mavenRepositoryScanner = mock[MavenRepositoryScanner]
    val dependencyUpgradeAnalyzer = mock[DependencyUpgradeAnalyzer]
    val upgrader = mock[Upgrader]
    val reporter = mock[Reporter]
    val notifications = mock[Notifications]
    val runner: Runner = new RunnerImpl(
      systemClock,
      pomFileScanner,
      mavenRepositoryScanner,
      dependencyUpgradeAnalyzer,
      upgrader,
      reporter,
      notifications)
    expecting {
      systemClock.currentTimeMillis.andReturn(arbitraryStartTime)
      pomFileScanner.scanExistingDependencies().andReturn(SampleData.existingDependencies)
      mavenRepositoryScanner.scanLatestDependencies(SampleData.existingDependencies).andReturn(SampleData.latestDependencies)
      dependencyUpgradeAnalyzer.outOfDate(SampleData.existingDependencies, SampleData.latestDependencies).andReturn(SampleData.outOfDate)
      upgrader.performAutomaticUpgrades(SampleData.outOfDate).andReturn(SampleData.automaticUpgradesPerformed)
      systemClock.currentTimeMillis.andReturn(arbitraryEndTime)
      reporter.reportAutomaticUpgradesPerformed(SampleData.automaticUpgradesPerformed, arbitraryEndTime)
      reporter.reportOutOfDate(SampleData.outOfDate)
      notifications.timeTaken(arbitraryStartTime, arbitraryEndTime)
    }
    whenExecuting(systemClock, pomFileScanner, mavenRepositoryScanner, dependencyUpgradeAnalyzer, upgrader, reporter, notifications) {
      runner.run()
    }
  }
}
