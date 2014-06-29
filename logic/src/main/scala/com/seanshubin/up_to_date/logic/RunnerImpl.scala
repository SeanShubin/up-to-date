package com.seanshubin.up_to_date.logic

class RunnerImpl(systemClock: SystemClock,
                 pomFileScanner: PomFileScanner,
                 mavenRepositoryScanner: MavenRepositoryScanner,
                 dependencyUpgradeAnalyzer: DependencyUpgradeAnalyzer,
                 upgrader: Upgrader,
                 reporter: Reporter,
                 notifications: Notifications) extends Runner {
  override def run(): Unit = {
    val startTime = systemClock.currentTimeMillis
    val existingDependencies = pomFileScanner.scanExistingDependencies()
    val latestDependencies = mavenRepositoryScanner.scanLatestDependencies(existingDependencies.toGroupAndArtifactSet)
    val outOfDate = dependencyUpgradeAnalyzer.outOfDate(existingDependencies, latestDependencies)
    val automaticUpgradesPerformed = upgrader.performAutomaticUpgrades(outOfDate)
    val endTime = systemClock.currentTimeMillis
    reporter.reportAutomaticUpgradesPerformed(automaticUpgradesPerformed, endTime)
    reporter.reportOutOfDate(outOfDate)
    notifications.timeTaken(startTime, endTime)
  }
}
