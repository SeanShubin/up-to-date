package com.seanshubin.up_to_date.logic

import java.nio.file.Path

class ReporterImpl(reportPath: Path,
                   pomReportName: String,
                   repositoryReportName: String,
                   inconsistencyReportName: String,
                   upgradesToApplyReportName: String,
                   upgradesToIgnoreReportName: String,
                   statusQuoReportName: String,
                   fileSystem: FileSystem,
                   jsonMarshaller: JsonMarshaller) extends Reporter {
  override def reportStatusQuo(upgrades: Seq[Upgrade]): Unit = {
    val upgradesAsGroupArtifactSeq = upgrades.map(_.toGroupArtifactSeq)
    val jsonReport = jsonMarshaller.toJson(upgradesAsGroupArtifactSeq)
    fileSystem.ensureDirectoriesExist(reportPath)
    fileSystem.storeString(reportPath.resolve(statusQuoReportName), jsonReport)
  }

  override def reportUpgradesToApply(upgrades: Seq[Upgrade]): Unit = {
    val upgradesByPom = Upgrade.groupByLocation(upgrades)
    val jsonReport = jsonMarshaller.toJson(upgradesByPom)
    fileSystem.ensureDirectoriesExist(reportPath)
    fileSystem.storeString(reportPath.resolve(upgradesToApplyReportName), jsonReport)
  }

  override def reportUpgradesToIgnore(upgrades: Seq[Upgrade]): Unit = {
    val upgradesByPom = Upgrade.groupByLocation(upgrades)
    val jsonReport = jsonMarshaller.toJson(upgradesByPom)
    fileSystem.ensureDirectoriesExist(reportPath)
    fileSystem.storeString(reportPath.resolve(upgradesToIgnoreReportName), jsonReport)
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
