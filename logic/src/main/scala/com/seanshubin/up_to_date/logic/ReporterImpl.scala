package com.seanshubin.up_to_date.logic

import java.nio.file.Path

class ReporterImpl(reportPath: Path,
                   observationReportName: String,
                   recommendationReportName: String,
                   fileSystem: FileSystem,
                   jsonMarshaller: JsonMarshaller) extends Reporter {
  override def reportAutomaticUpgradesPerformed(automaticUpgradesPerformed: AutomaticUpgradesPerformed): Unit = {
    println("reporter - Automatic upgrades not supported yet, skipping.")
  }

  override def reportRecommendations(recommendations: Recommendations): Unit = {
    val jsonReport = jsonMarshaller.toJson(recommendations)
    fileSystem.ensureDirectoriesExist(reportPath)
    fileSystem.storeString(reportPath.resolve(recommendationReportName), jsonReport)
  }

  override def reportObservations(existingDependencies: ExistingDependencies,
                                  dependencyVersions: DependencyVersions): Unit = {
    val observations = Observations(existingDependencies.byPom, dependencyVersions.map)
    val jsonReport = jsonMarshaller.toJson(observations)
    fileSystem.ensureDirectoriesExist(reportPath)
    fileSystem.storeString(reportPath.resolve(observationReportName), jsonReport)
  }
}
