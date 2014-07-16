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
      mavenRepositoryScanner.scanLatestDependencies(SampleData.existingDependencies.toGroupAndArtifactSet).andReturn(SampleData.dependencyVersions)
      dependencyUpgradeAnalyzer.recommend(SampleData.existingDependencies, SampleData.dependencyVersions).andReturn(SampleData.recommendations)
      upgrader.performAutomaticUpgrades(SampleData.recommendations).andReturn(SampleData.automaticUpgradesPerformed)
      reporter.reportPom(SampleData.existingDependencies)
      reporter.reportRepository(SampleData.dependencyVersions)
      reporter.reportAutomaticUpgradesPerformed(SampleData.automaticUpgradesPerformed)
      reporter.reportRecommendations(SampleData.recommendations)
    }
    whenExecuting(pomFileScanner, mavenRepositoryScanner, dependencyUpgradeAnalyzer, upgrader, reporter) {
      runner.run()
    }
    assert(notifications.timeTakenCalls === Seq("Total Time Taken"))
  }
}
