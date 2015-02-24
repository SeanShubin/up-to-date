package com.seanshubin.up_to_date.logic

import java.nio.file.Path

import com.seanshubin.devon.core.devon.DevonMarshaller

import scala.reflect.runtime.universe

class ReporterImpl(reportPath: Path,
                   reportNames: ReportNames,
                   fileSystem: FileSystem,
                   jsonMarshaller: JsonMarshaller,
                   devonMarshaller: DevonMarshaller) extends Reporter {
  override def reportStatusQuo(upgrades: Seq[Upgrade]): Unit = {
    val upgradesAsGroupArtifactSeq = upgrades.map(_.toGroupArtifactSeq)
    generateReport(upgradesAsGroupArtifactSeq, reportNames.statusQuo)
  }

  override def reportUpgradesToApply(upgrades: Seq[Upgrade]): Unit = {
    val upgradesByPom = Upgrade.groupByLocation(upgrades)
    generateReport(upgradesByPom, reportNames.upgradesToApply)
  }

  override def reportUpgradesToIgnore(upgrades: Seq[Upgrade]): Unit = {
    val upgradesByPom = Upgrade.groupByLocation(upgrades)
    generateReport(upgradesByPom, reportNames.upgradesToIgnore)
  }

  override def reportInconsistencies(inconsistencies: Map[GroupAndArtifact, Seq[Dependency]]): Unit = {
    generateReport(inconsistencies, reportNames.inconsistency)
  }

  override def reportPom(poms: Seq[Pom]): Unit = {
    val pomByLocation = Pom.groupByLocation(poms)
    generateReport(pomByLocation, reportNames.pom)
  }

  override def reportRepository(libraries: Seq[Library]): Unit = {
    val librariesByLocation = Library.groupByLocation(libraries)
    generateReport(librariesByLocation, reportNames.repository)
  }

  override def reportNotFound(notFound: Seq[GroupAndArtifact]): Unit = {
    generateReport(notFound, reportNames.notFound)
  }

  private def generateReport[T: universe.TypeTag](value: T, reportName: String): Unit = {
    val jsonReport = jsonMarshaller.toJson(value)
    val devon = devonMarshaller.fromValue(value)
    val devonReport = devonMarshaller.toPretty(devon)
    fileSystem.ensureDirectoriesExist(reportPath)
    fileSystem.storeString(reportPath.resolve(reportName + ".json"), jsonReport)
    fileSystem.storeLines(reportPath.resolve(reportName + ".txt"), devonReport)
  }
}
