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
    val doNotUpgradeFrom = Set(GroupAndArtifact("from-group", "from-artifact"))
    val doNotUpgradeTo = Set(GroupArtifactVersion("to-group", "to-artifact", "to-verions"))
    val runner: Runner = new RunnerImpl(
      pomFileScanner,
      mavenRepositoryScanner,
      dependencyUpgradeAnalyzer,
      doNotUpgradeFrom,
      doNotUpgradeTo,
      upgrader,
      reporter,
      notifications)
    expecting {
      pomFileScanner.scanPomFiles().andReturn(SampleData.poms)
      mavenRepositoryScanner.scanLatestDependencies(SampleData.poms).andReturn(SampleData.libraries)
      dependencyUpgradeAnalyzer.findInconsistencies(SampleData.poms).andReturn(SampleData.inconsistencies)
      dependencyUpgradeAnalyzer.recommendUpgrades(SampleData.poms, SampleData.libraries).andReturn(SampleData.upgrades)
      dependencyUpgradeAnalyzer.splitIntoApplyAndIgnore(SampleData.upgrades, doNotUpgradeFrom, doNotUpgradeTo).andReturn((SampleData.applyUpgrades, SampleData.ignoreUpgrades))
      upgrader.performAutomaticUpgradesIfApplicable(SampleData.applyUpgrades)
      reporter.reportPom(SampleData.poms)
      reporter.reportRepository(SampleData.libraries)
      reporter.reportUpgradesToApply(SampleData.applyUpgrades)
      reporter.reportUpgradesToIgnore(SampleData.ignoreUpgrades)
      reporter.reportInconsistencies(SampleData.inconsistencies)
      reporter.reportStatusQuo(SampleData.upgrades)
    }
    whenExecuting(pomFileScanner, mavenRepositoryScanner, dependencyUpgradeAnalyzer, upgrader, reporter) {
      runner.run()
    }
    assert(notifications.timeTakenCalls === Seq("Total Time Taken"))
  }
}
