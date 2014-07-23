package com.seanshubin.up_to_date.logic

import java.nio.file.Path

class ReporterImpl(reportPath: Path,
                   pomReportName: String,
                   repositoryReportName: String,
                   recommendationReportName: String,
                   inconsistencyReportName: String,
                   fileSystem: FileSystem,
                   jsonMarshaller: JsonMarshaller) extends Reporter {
  override def reportAutomaticUpgradesPerformed(automaticUpgradesPerformed: AutomaticUpgradesPerformed): Unit = {
    println("reporter - Automatic upgrades not supported yet, skipping.")
  }

  override def reportRecommendations(recommendations: Recommendations): Unit = {
    val jsonReport = jsonMarshaller.toJson(recommendations.filterWithRecommendation)
    fileSystem.ensureDirectoriesExist(reportPath)
    fileSystem.storeString(reportPath.resolve(recommendationReportName), jsonReport)
  }

  override def reportInconsistencies(recommendations: Recommendations): Unit = {
    val jsonReport = jsonMarshaller.toJson(recommendations.filterWithInconsistent)
    fileSystem.ensureDirectoriesExist(reportPath)
    fileSystem.storeString(reportPath.resolve(inconsistencyReportName), jsonReport)
  }

  override def reportPom(existingDependencies: ExistingDependencies): Unit = {
    val jsonReport = jsonMarshaller.toJson(existingDependencies.byPom)
    fileSystem.ensureDirectoriesExist(reportPath)
    fileSystem.storeString(reportPath.resolve(pomReportName), jsonReport)
  }

  override def reportRepository(dependencyVersions: DependencyVersions): Unit = {
    val jsonReport = jsonMarshaller.toJson(dependencyVersions.map)
    fileSystem.ensureDirectoriesExist(reportPath)
    fileSystem.storeString(reportPath.resolve(repositoryReportName), jsonReport)
  }
}
