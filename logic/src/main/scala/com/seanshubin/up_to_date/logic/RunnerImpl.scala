package com.seanshubin.up_to_date.logic

class RunnerImpl(pomFileScanner: PomFileScanner,
                 mavenRepositoryScanner: MavenRepositoryScanner,
                 dependencyUpgradeAnalyzer: DependencyUpgradeAnalyzer,
                 upgrader: PomFileUpgrader,
                 reporter: Reporter,
                 notifications: Notifications) extends Runner {
  override def run(): Unit = {
    notifications.timeTaken("Total Time Taken") {
      val poms = pomFileScanner.scanPomFiles()
      val libraries = mavenRepositoryScanner.scanLatestDependencies(poms)
      val inconsistencies = dependencyUpgradeAnalyzer.findInconsistencies(poms)
      val upgrades = dependencyUpgradeAnalyzer.recommendUpgrades(poms, libraries)
      reporter.reportPom(poms)
      reporter.reportRepository(libraries)
      upgrader.performAutomaticUpgradesIfApplicable(upgrades)
      reporter.reportUpgrades(upgrades)
      reporter.reportInconsistencies(inconsistencies)
    }
  }
}
