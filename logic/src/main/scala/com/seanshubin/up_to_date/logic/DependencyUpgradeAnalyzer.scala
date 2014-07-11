package com.seanshubin.up_to_date.logic

trait DependencyUpgradeAnalyzer {
  def outOfDate(existingDependencies: ExistingDependencies, latestDependencies: DependencyVersions): OutOfDateReport
}
