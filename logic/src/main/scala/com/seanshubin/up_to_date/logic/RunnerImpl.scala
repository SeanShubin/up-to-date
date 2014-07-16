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
      reporter.reportPom(existingDependencies)
      reporter.reportRepository(latestDependencies)
      val recommendations = dependencyUpgradeAnalyzer.recommend(existingDependencies, latestDependencies)
      val automaticUpgradesPerformed = upgrader.performAutomaticUpgrades(recommendations)
      reporter.reportAutomaticUpgradesPerformed(automaticUpgradesPerformed)
      reporter.reportRecommendations(recommendations)
    }
  }
}
