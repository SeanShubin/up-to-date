package com.seanshubin.up_to_date.logic

class RunnerImpl(pomFileScanner: PomFileScanner,
                 mavenRepositoryScanner: MavenRepositoryScanner,
                 dependencyUpgradeAnalyzer: DependencyUpgradeAnalyzer,
                 doNotUpgradeFrom: Set[GroupAndArtifact],
                 doNotUpgradeTo: Set[GroupArtifactVersion],
                 upgrader: PomFileUpgrader,
                 reporter: Reporter,
                 notifications: Notifications) extends Runner {
  override def run(): Unit = {
    notifications.timeTaken("Total Time Taken") {
      val poms = pomFileScanner.scanPomFiles()
      val libraries = mavenRepositoryScanner.scanLatestDependencies(poms)
      val inconsistencies = dependencyUpgradeAnalyzer.findInconsistencies(poms)
      val upgrades = dependencyUpgradeAnalyzer.recommendUpgrades(poms, libraries)
      val (apply, ignore) = dependencyUpgradeAnalyzer.splitIntoApplyAndIgnore(upgrades, doNotUpgradeFrom, doNotUpgradeTo)
      upgrader.performAutomaticUpgradesIfApplicable(apply)
      reporter.reportPom(poms)
      reporter.reportRepository(libraries)
      reporter.reportUpgradesToApply(apply)
      reporter.reportUpgradesToIgnore(ignore)
      reporter.reportInconsistencies(inconsistencies)
      reporter.reportStatusQuo(upgrades)
    }
  }
}
