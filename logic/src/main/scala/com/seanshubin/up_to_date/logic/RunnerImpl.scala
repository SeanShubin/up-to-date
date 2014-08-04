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
      reporter.reportPom(poms)
      val (libraries, notFound) = mavenRepositoryScanner.scanLatestDependencies(poms)
      reporter.reportRepository(libraries)
      reporter.reportNotFound(notFound)
      val inconsistencies = dependencyUpgradeAnalyzer.findInconsistencies(poms)
      reporter.reportInconsistencies(inconsistencies)
      val upgrades = dependencyUpgradeAnalyzer.recommendUpgrades(poms, libraries)
      reporter.reportStatusQuo(upgrades)
      val (apply, ignore) = dependencyUpgradeAnalyzer.splitIntoApplyAndIgnore(upgrades, doNotUpgradeFrom, doNotUpgradeTo)
      reporter.reportUpgradesToApply(apply)
      reporter.reportUpgradesToIgnore(ignore)
      upgrader.performAutomaticUpgradesIfApplicable(apply)
    }
  }
}
