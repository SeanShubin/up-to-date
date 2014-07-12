package com.seanshubin.up_to_date.logic

trait DependencyUpgradeAnalyzer {
  def recommend(existingDependencies: ExistingDependencies, latestDependencies: DependencyVersions): Recommendations
}
