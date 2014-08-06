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
    val samplePoms = SampleData.poms()
    val sampleLibraries = SampleData.libraries()
    val sampleNotFound = SampleData.groupAndArtifactSeq()
    val sampleInconsistencies = SampleData.inconsistencies()
    val sampleUpgrades = SampleData.upgrades(1, 6)
    val sampleApplyUpgrades = SampleData.upgrades(1, 3)
    val sampleIgnoreUpgrades = SampleData.upgrades(4, 6)
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
      pomFileScanner.scanPomFiles().andReturn(samplePoms)
      mavenRepositoryScanner.scanLatestDependencies(samplePoms).andReturn((sampleLibraries, sampleNotFound))
      dependencyUpgradeAnalyzer.findInconsistencies(samplePoms).andReturn(sampleInconsistencies)
      dependencyUpgradeAnalyzer.recommendUpgrades(samplePoms, sampleLibraries).andReturn(sampleUpgrades)
      dependencyUpgradeAnalyzer.splitIntoApplyAndIgnore(sampleUpgrades, doNotUpgradeFrom, doNotUpgradeTo).andReturn((sampleApplyUpgrades, sampleIgnoreUpgrades))
      upgrader.performAutomaticUpgradesIfApplicable(sampleApplyUpgrades)
      reporter.reportPom(samplePoms)
      reporter.reportRepository(sampleLibraries)
      reporter.reportUpgradesToApply(sampleApplyUpgrades)
      reporter.reportUpgradesToIgnore(sampleIgnoreUpgrades)
      reporter.reportInconsistencies(sampleInconsistencies)
      reporter.reportStatusQuo(sampleUpgrades)
      reporter.reportNotFound(sampleNotFound)
    }
    whenExecuting(pomFileScanner, mavenRepositoryScanner, dependencyUpgradeAnalyzer, upgrader, reporter) {
      runner.run()
    }
    assert(notifications.timeTakenCalls === Seq("Total Time Taken"))
  }
}
