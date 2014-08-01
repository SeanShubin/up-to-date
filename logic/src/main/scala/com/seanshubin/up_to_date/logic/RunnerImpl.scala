package com.seanshubin.up_to_date.logic

class RunnerImpl(pomFileScanner: PomFileScanner,
                 mavenRepositoryScanner: MavenRepositoryScanner,
                 dependencyUpgradeAnalyzer: DependencyUpgradeAnalyzer,
                 upgrader: PomFileUpgrader,
                 reporter: Reporter,
                 notifications: Notifications) extends Runner {
  override def run(): Unit = {
    notifications.timeTaken("Total Time Taken") {
      val existingDependencies = pomFileScanner.scanExistingDependencies()
      val latestDependencies = mavenRepositoryScanner.scanLatestDependencies(existingDependencies.toGroupAndArtifactSet)
      reporter.reportPom(existingDependencies)
      reporter.reportRepository(latestDependencies)
      val recommendations = dependencyUpgradeAnalyzer.recommend(existingDependencies, latestDependencies)
      val upgradesByPom = recommendations.upgradesByPom
      upgrader.performAutomaticUpgradesIfApplicable(upgradesByPom)
      reporter.reportAutomaticUpgradesPerformed(upgradesByPom)
      reporter.reportRecommendations(recommendations)
      reporter.reportInconsistencies(recommendations)
    }
  }
}
