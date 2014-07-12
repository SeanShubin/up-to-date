package com.seanshubin.up_to_date.logic

trait Reporter {
  def reportAutomaticUpgradesPerformed(automaticUpgradesPerformed: AutomaticUpgradesPerformed)

  def reportRecommendations(recommendations: Recommendations)

  def reportObservations(existingDependencies: ExistingDependencies, dependencyVersions: DependencyVersions)
}
