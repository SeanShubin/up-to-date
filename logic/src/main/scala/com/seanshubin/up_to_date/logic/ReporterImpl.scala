package com.seanshubin.up_to_date.logic

import java.nio.file.Path

class ReporterImpl(reportPath: Path,
                   pomReportName: String,
                   repositoryReportName: String,
                   recommendationReportName: String,
                   inconsistencyReportName: String,
                   upgradesReportName: String,
                   fileSystem: FileSystem,
                   jsonMarshaller: JsonMarshaller) extends Reporter {
  override def reportUpgrades(upgrades: Seq[Upgrade]): Unit = {
    val upgradesByPom = Upgrade.groupByLocation(upgrades)
    val jsonReport = jsonMarshaller.toJson(upgradesByPom)
    fileSystem.ensureDirectoriesExist(reportPath)
    fileSystem.storeString(reportPath.resolve(upgradesReportName), jsonReport)
  }

  override def reportInconsistencies(inconsistencies: Map[GroupAndArtifact, Seq[Dependency]]): Unit = {
    val jsonReport = jsonMarshaller.toJson(inconsistencies)
    fileSystem.ensureDirectoriesExist(reportPath)
    fileSystem.storeString(reportPath.resolve(inconsistencyReportName), jsonReport)
  }

  override def reportPom(poms: Seq[Pom]): Unit = {
    val pomByLocation = Pom.groupByLocation(poms)
    val jsonReport = jsonMarshaller.toJson(pomByLocation)
    fileSystem.ensureDirectoriesExist(reportPath)
    fileSystem.storeString(reportPath.resolve(pomReportName), jsonReport)
  }

  override def reportRepository(libraries: Seq[Library]): Unit = {
    val librariesByLocation = Library.groupByLocation(libraries)
    val jsonReport = jsonMarshaller.toJson(librariesByLocation)
    fileSystem.ensureDirectoriesExist(reportPath)
    fileSystem.storeString(reportPath.resolve(repositoryReportName), jsonReport)
  }
}
