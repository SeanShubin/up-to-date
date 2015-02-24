package com.seanshubin.up_to_date.logic

import java.nio.file.Path

class ReporterImpl(reportPath: Path,
                   reportNames: ReportNames,
                   fileSystem: FileSystem,
                   jsonMarshaller: JsonMarshaller) extends Reporter {
  override def reportStatusQuo(upgrades: Seq[Upgrade]): Unit = {
    val upgradesAsGroupArtifactSeq = upgrades.map(_.toGroupArtifactSeq)
    val jsonReport = jsonMarshaller.toJson(upgradesAsGroupArtifactSeq)
    fileSystem.ensureDirectoriesExist(reportPath)
    fileSystem.storeString(jsonPath(reportNames.statusQuo), jsonReport)
  }

  override def reportUpgradesToApply(upgrades: Seq[Upgrade]): Unit = {
    val upgradesByPom = Upgrade.groupByLocation(upgrades)
    val jsonReport = jsonMarshaller.toJson(upgradesByPom)
    fileSystem.ensureDirectoriesExist(reportPath)
    fileSystem.storeString(jsonPath(reportNames.upgradesToApply), jsonReport)
  }

  override def reportUpgradesToIgnore(upgrades: Seq[Upgrade]): Unit = {
    val upgradesByPom = Upgrade.groupByLocation(upgrades)
    val jsonReport = jsonMarshaller.toJson(upgradesByPom)
    fileSystem.ensureDirectoriesExist(reportPath)
    fileSystem.storeString(jsonPath(reportNames.upgradesToIgnore), jsonReport)
  }

  override def reportInconsistencies(inconsistencies: Map[GroupAndArtifact, Seq[Dependency]]): Unit = {
    val jsonReport = jsonMarshaller.toJson(inconsistencies)
    fileSystem.ensureDirectoriesExist(reportPath)
    fileSystem.storeString(jsonPath(reportNames.inconsistency), jsonReport)
  }

  override def reportPom(poms: Seq[Pom]): Unit = {
    val pomByLocation = Pom.groupByLocation(poms)
    val jsonReport = jsonMarshaller.toJson(pomByLocation)
    fileSystem.ensureDirectoriesExist(reportPath)
    fileSystem.storeString(jsonPath(reportNames.pom), jsonReport)
  }

  override def reportRepository(libraries: Seq[Library]): Unit = {
    val librariesByLocation = Library.groupByLocation(libraries)
    val jsonReport = jsonMarshaller.toJson(librariesByLocation)
    fileSystem.ensureDirectoriesExist(reportPath)
    fileSystem.storeString(jsonPath(reportNames.repository), jsonReport)
  }

  override def reportNotFound(notFound: Seq[GroupAndArtifact]): Unit = {
    val jsonReport = jsonMarshaller.toJson(notFound)
    fileSystem.ensureDirectoriesExist(reportPath)
    fileSystem.storeString(jsonPath(reportNames.notFound), jsonReport)
  }

  private def jsonPath(name: String): Path = reportPath.resolve(name + ".json")
}
