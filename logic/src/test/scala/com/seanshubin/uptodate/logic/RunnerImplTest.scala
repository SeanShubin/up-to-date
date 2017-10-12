package com.seanshubin.uptodate.logic

import org.scalatest.FunSuite
import org.scalatest.easymock.EasyMockSugar

class RunnerImplTest extends FunSuite with EasyMockSugar {
  test("application flow") {
    val pomFileScanner = mock[PomFileScanner]
    val mavenRepositoryScanner = mock[MavenRepositoryScanner]
    val dependencyUpgradeAnalyzer = mock[DependencyUpgradeAnalyzer]
    val upgrader = mock[PomFileUpgrader]
    val reporter = mock[Reporter]
    val notifications = new StubNotifications
    val doNotUpgradeFrom = Set(GroupAndArtifact("from-group", "from-artifact"))
    val doNotUpgradeTo = Set(GroupArtifactVersion("to-group", "to-artifact", "to-verions"))
    val samplePoms = SampleData.poms()
    val sampleLibraries = SampleData.libraries()
    val sampleNotFound = SampleData.groupAndArtifactSeq()
    val sampleInconsistencies = SampleData.inconsistencies()
    val sampleUpgrades = SampleData.upgrades(1, 6)
    val sampleApplyUpgrades = SampleData.upgrades(1, 3)
    val sampleIgnoreUpgrades = SampleData.upgrades(4, 6)
    val sampleByDependency = SampleData.byDependency()
    val sampleDependencies = SampleData.dependencies(1, "foo")
    val sampleSummary = SampleData.summary()
    val runner: Runnable = new RunnerImpl(
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
      reporter.reportUnexpandedPom(samplePoms)
      reporter.reportPom(samplePoms)
      reporter.reportPropertyConflicts(Map())
      mavenRepositoryScanner.scanLatestDependencies(samplePoms).andReturn((sampleLibraries, sampleNotFound))
      reporter.reportRepository(sampleLibraries)
      reporter.reportNotFound(sampleNotFound)
      dependencyUpgradeAnalyzer.findInconsistencies(samplePoms).andReturn(sampleInconsistencies)
      reporter.reportInconsistencies(sampleInconsistencies)
      dependencyUpgradeAnalyzer.recommendUpgrades(samplePoms, sampleLibraries).andReturn(sampleUpgrades)
      reporter.reportStatusQuo(sampleUpgrades)
      dependencyUpgradeAnalyzer.splitIntoApplyAndIgnore(sampleUpgrades, doNotUpgradeFrom, doNotUpgradeTo).andReturn((sampleApplyUpgrades, sampleIgnoreUpgrades))
      reporter.reportUpgradesToApply(sampleApplyUpgrades)
      reporter.reportUpgradesToIgnore(sampleIgnoreUpgrades)
      dependencyUpgradeAnalyzer.byDependency(sampleApplyUpgrades).andReturn(sampleByDependency)
      reporter.reportByDependency(sampleByDependency)
      dependencyUpgradeAnalyzer.alreadyUpToDate(samplePoms, sampleLibraries).andReturn(sampleDependencies)
      dependencyUpgradeAnalyzer.summary(samplePoms, sampleByDependency, sampleNotFound, sampleApplyUpgrades, sampleIgnoreUpgrades, sampleDependencies).andReturn(sampleSummary)
      reporter.reportSummary(sampleSummary)
      upgrader.performAutomaticUpgradesIfApplicable(sampleApplyUpgrades)
    }
    whenExecuting(pomFileScanner, mavenRepositoryScanner, dependencyUpgradeAnalyzer, upgrader, reporter) {
      runner.run()
    }
    assert(notifications.timeTakenCalls === Seq("Total Time Taken"))
  }
}

