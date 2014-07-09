package com.seanshubin.up_to_date.logic

class ReporterImpl extends Reporter {
  override def reportAutomaticUpgradesPerformed(automaticUpgradesPerformed: AutomaticUpgradesPerformed): Unit = ???

  override def reportOutOfDate(outOfDate: OutOfDate): Unit = ???

  override def reportObservations(existingDependencies: ExistingDependencies, dependencyVersions: DependencyVersions): Unit = ???
}
