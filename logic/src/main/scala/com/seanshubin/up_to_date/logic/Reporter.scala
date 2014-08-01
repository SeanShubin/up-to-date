package com.seanshubin.up_to_date.logic

trait Reporter {
  def reportAutomaticUpgradesPerformed(upgradesByPom: Map[String, Map[GroupAndArtifact, String]])

  def reportRecommendations(recommendations: Recommendations)

  def reportInconsistencies(recommendations: Recommendations)

  def reportPom(existingDependencies: ExistingDependencies)

  def reportRepository(dependencyVersions: DependencyVersions)
}
