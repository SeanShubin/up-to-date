package com.seanshubin.up_to_date.logic

import java.nio.file.Path

import scala.collection.immutable.ListMap
import scala.reflect.runtime.universe

class ReporterImpl(reportPath: Path,
                   reportNames: ReportNames,
                   fileSystemReportGenerator: FileSystemReportGenerator) extends Reporter {
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

  override def reportByDependency(artifactToUpgrade: Map[GroupAndArtifact, List[Upgrade]]): Unit = {
    def addEntry(map:Map[GroupAndArtifact, List[Upgrade]], key:GroupAndArtifact): Map[GroupAndArtifact, List[Upgrade]] = {
      map + (key -> artifactToUpgrade(key))
    }
    val empty:Map[GroupAndArtifact, List[Upgrade]] = ListMap.empty
    val artifactsInKeyOrder = artifactToUpgrade.keys.toSeq.sorted.foldLeft(empty)(addEntry)
    generateReport(artifactsInKeyOrder, reportNames.byDependency)
  }

  override def reportSummary(summary: SummaryReport): Unit = {
    generateReport(summary, reportNames.summary)
  }

  private def generateReport[T: universe.TypeTag](value: T, reportName: String): Unit = {
    fileSystemReportGenerator.sendReportToFileSystem(value, reportName)
  }
}
