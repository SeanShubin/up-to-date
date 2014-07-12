package com.seanshubin.up_to_date.logic

import java.nio.file.Path

class ReporterImpl(observationPath: Path, fileSystem: FileSystem, jsonMarshaller: JsonMarshaller) extends Reporter {
  override def reportAutomaticUpgradesPerformed(automaticUpgradesPerformed: AutomaticUpgradesPerformed): Unit = ???

  override def reportRecommendations(recommendations: Recommendations): Unit = ???

  override def reportObservations(existingDependencies: ExistingDependencies,
                                  dependencyVersions: DependencyVersions): Unit = {
    val observations = Observations(existingDependencies.byPom, dependencyVersions.map)
    val jsonReport = jsonMarshaller.toJson(observations)
    fileSystem.ensureDirectoriesExist(observationPath.getParent)
    fileSystem.storeString(observationPath, jsonReport)
  }
}
