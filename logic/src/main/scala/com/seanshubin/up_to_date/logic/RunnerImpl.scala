package com.seanshubin.up_to_date.logic

class RunnerImpl(pomFileScanner: PomFileScanner,
                 mavenRepositoryScanner: MavenRepositoryScanner,
                 dependencyUpgradeAnalyzer: DependencyUpgradeAnalyzer,
                 upgrader: Upgrader,
                 reporter: Reporter,
                 notifications: Notifications) extends Runner {
  override def run(): Unit = {
    notifications.timeTaken("Total Time Taken") {
      val existingDependencies = pomFileScanner.scanExistingDependencies()
      val latestDependencies = mavenRepositoryScanner.scanLatestDependencies(existingDependencies.toGroupAndArtifactSet)
      reporter.reportObservations(existingDependencies, latestDependencies)
      val outOfDate = dependencyUpgradeAnalyzer.outOfDate(existingDependencies, latestDependencies)
      val automaticUpgradesPerformed = upgrader.performAutomaticUpgrades(outOfDate)
      reporter.reportAutomaticUpgradesPerformed(automaticUpgradesPerformed)
      reporter.reportOutOfDate(outOfDate)
    }
  }
}
