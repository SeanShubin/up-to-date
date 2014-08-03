package com.seanshubin.up_to_date.logic

import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

class RunnerImplTest extends FunSuite with EasyMockSugar {
  test("application flow") {
    val pomFileScanner = mock[PomFileScanner]
    val mavenRepositoryScanner = mock[MavenRepositoryScanner]
    val dependencyUpgradeAnalyzer = mock[DependencyUpgradeAnalyzer]
    val upgrader = mock[PomFileUpgrader]
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
      pomFileScanner.scanPomFiles().andReturn(SampleData.poms)
      mavenRepositoryScanner.scanLatestDependencies(SampleData.poms).andReturn(SampleData.libraries)
      dependencyUpgradeAnalyzer.findInconsistencies(SampleData.poms).andReturn(SampleData.inconsistencies)
      dependencyUpgradeAnalyzer.recommendUpgrades(SampleData.poms, SampleData.libraries).andReturn(SampleData.upgrades)
      upgrader.performAutomaticUpgradesIfApplicable(SampleData.upgrades)
      reporter.reportPom(SampleData.poms)
      reporter.reportRepository(SampleData.libraries)
      reporter.reportUpgrades(SampleData.upgrades)
      reporter.reportInconsistencies(SampleData.inconsistencies)
    }
    whenExecuting(pomFileScanner, mavenRepositoryScanner, dependencyUpgradeAnalyzer, upgrader, reporter) {
      runner.run()
    }
    assert(notifications.timeTakenCalls === Seq("Total Time Taken"))
  }
}
