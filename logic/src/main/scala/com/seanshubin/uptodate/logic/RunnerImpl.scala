package com.seanshubin.uptodate.logic

class RunnerImpl(pomFileScanner: PomFileScanner,
                 mavenRepositoryScanner: MavenRepositoryScanner,
                 dependencyUpgradeAnalyzer: DependencyUpgradeAnalyzer,
                 doNotUpgradeFrom: Set[GroupAndArtifact],
                 doNotUpgradeTo: Set[GroupArtifactVersion],
                 upgrader: PomFileUpgrader,
                 reporter: Reporter,
                 notifications: Notifications) extends Runnable {
  override def run(): Unit = {
    notifications.timeTaken("Total Time Taken") {
      val unexpandedPoms = pomFileScanner.scanPomFiles()
      reporter.reportUnexpandedPom(unexpandedPoms)
      val (poms, propertyConflicts) = Pom.applyExpansions(unexpandedPoms)
      reporter.reportPom(poms)
      reporter.reportPropertyConflicts(propertyConflicts)
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
      val byDependency = dependencyUpgradeAnalyzer.byDependency(apply)
      reporter.reportByDependency(byDependency)
      val alreadyUpToDate = dependencyUpgradeAnalyzer.alreadyUpToDate(poms, libraries)
      val summary = dependencyUpgradeAnalyzer.summary(poms, byDependency, notFound, apply, ignore, alreadyUpToDate)
      reporter.reportSummary(summary)
      upgrader.performAutomaticUpgradesIfApplicable(apply)
    }
  }
}
