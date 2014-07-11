package com.seanshubin.up_to_date.logic

trait Reporter {
  def reportAutomaticUpgradesPerformed(automaticUpgradesPerformed: AutomaticUpgradesPerformed)

  def reportOutOfDate(outOfDate: OutOfDateReport)

  def reportObservations(existingDependencies: ExistingDependencies, dependencyVersions: DependencyVersions)
}
